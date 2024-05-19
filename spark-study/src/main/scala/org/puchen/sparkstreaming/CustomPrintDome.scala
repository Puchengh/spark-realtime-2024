package org.puchen.sparkstreaming

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.puchen.Util.SSC

import java.sql.{Connection, DriverManager, PreparedStatement, Timestamp}

object CustomPrintDome {

  /**
   * 每隔10s计算最近20s的热搜词
   */
  def main(args: Array[String]): Unit = {

    val ssc: StreamingContext = SSC.create("spark2", 10)
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)

    val resultDS: DStream[(String, Int)] = lines.flatMap(_.split(" "))
      .map((_, 1))
      //windowDuration 窗口长度 窗口大小 表示要计算最近多长时间的数据
      //slideDuration  滑动间隔 表示每个多长时间计算一次
      //注意windowDuration和slideDuration 必须是batchDuration的倍数
      .reduceByKeyAndWindow((a: Int, b: Int) => a + b, Seconds(20), Seconds(10))

    //对底层的rdd操作 并且返回结果
    val sortRDD: DStream[(String, Int)] = resultDS.transform(rdd => {
      val rddSort: RDD[(String, Int)] = rdd.sortBy(_._2, false)
      val tuples: Array[(String, Int)] = rddSort.take(3)
      println("=====top3 start======")
      tuples.foreach(println)
      println("=====top3 end======")
      rddSort
    })


    sortRDD.print()
    sortRDD.foreachRDD((rdd, time) => {
      val milliseconds: Long = time.milliseconds
      println("-----自定义输出start-----")
      println("batchL：" + milliseconds)
      println("-----自定义输出end-----")

      //结果数据控制台
      rdd.foreach(println)
      rdd.coalesce(1).saveAsTextFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\result" + milliseconds)
      //      rdd.foreachPartition(iter => {
      //        //开启连接3
      //        val conn: Connection = DriverManager.getConnection("jdbc:myqsl://local:3306/text?characterEncoding=UTF-8", "test", "123456")
      //        // 关闭连接
      //        val sql = "insert into `test` (time,word,count) values(?,?,?)"
      //        val ps: PreparedStatement = conn.prepareStatement(sql)
      //        iter.foreach(t => {
      //          val word: String = t._1
      //          val count: Int = t._2
      //          ps.setTimestamp(1, new Timestamp(milliseconds))
      //          ps.setString(2, word)
      //          ps.setInt(3, count)
      //
      //          ps.addBatch()
      //        } )
      //        ps.executeBatch()
      //        if (conn != null) conn.close()
      //        if (ps != null) ps.close()
      //    })
    })
    ssc.start()
    ssc.awaitTermination()
    //优雅的关闭
    ssc.stop(stopSparkContext = true, stopGracefully = true)

  }
}
