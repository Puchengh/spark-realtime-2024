package org.puchen.dome

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object DstreamDome {

  def main(args: Array[String]): Unit = {


    val ssc: StreamingContext = new StreamingContext(new SparkConf().setMaster("local[*]").setAppName("spark"), Seconds(20))
    val value: ReceiverInputDStream[String] = ssc.socketTextStream("192.168.31.93", 9999)

    StorageLevel.MEMORY_AND_DISK_SER
    value.flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .print()

    ssc.start()
    ssc.awaitTermination()

  }

}
