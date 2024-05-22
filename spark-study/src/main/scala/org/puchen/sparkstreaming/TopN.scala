package org.puchen.sparkstreaming

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.puchen.util.SSC

object TopN {

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

    ssc.start()
    ssc.awaitTermination()
    //优雅的关闭
    ssc.stop(stopSparkContext = true, stopGracefully = true)


  }
}
