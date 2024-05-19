package org.puchen.sparkstreaming

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.puchen.Util.SSC

object SplitWindow {

  /**
   * 每隔5秒计算最近10秒的数据
   */
  def main(args: Array[String]): Unit = {

    val ssc: StreamingContext = SSC.create("spark", 5)
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)


    val resultDS: DStream[(String, Int)] = lines.flatMap(_.split(" "))
      .map((_, 1))
      //windowDuration 窗口长度 窗口大小 表示要计算最近多长时间的数据
      //slideDuration  滑动间隔 表示每个多长时间计算一次
      //注意windowDuration和slideDuration 必须是batchDuration的倍数
      .reduceByKeyAndWindow((a:Int, b:Int) => a + b, Seconds(10), Seconds(5))


    resultDS.print()

    ssc.start()
    ssc.awaitTermination()
    //优雅的关闭
    ssc.stop(stopSparkContext = true, stopGracefully = true)


  }
}
