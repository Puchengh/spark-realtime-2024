package org.puchen.sparkstreaming

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.puchen.util.SSC

object StatusRecover {

  def creatFun(): StreamingContext = {
    val ssc: StreamingContext = SSC.create("spark", 5)
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)

    //state存在文件中
    ssc.checkpoint("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\checkpoint")

    //定义一个函数用来处理状态  把当前数据和历史状态进行累加
    //历史维护是存在文件中，下次程序启动不会应用到
    val updateFunc = (currentValue: Seq[Int], hist: Option[Int]) => {
      if (currentValue.size > 0) {
        val result: Int = currentValue.sum + hist.getOrElse(0)
        Some(result)
      } else {
        hist
      }
    }
    val resultDS: DStream[(String, Int)] = lines.flatMap(_.split(" "))
      .map((_, 1))
      .updateStateByKey(updateFunc)
    resultDS.print()
    ssc
  }

  def main(args: Array[String]): Unit = {
    val ssc: StreamingContext = StreamingContext.getOrCreate("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\checkpoint", creatFun)
    ssc.sparkContext.setLogLevel("ERROR")
    ssc.start()
    ssc.awaitTermination()
    //优雅的关闭
    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}
