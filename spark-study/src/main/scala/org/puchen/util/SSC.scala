package org.puchen.util

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object SSC {
  def create(name: String, time: Int): StreamingContext = {
    val ssc: StreamingContext = new StreamingContext(
      new SparkContext(new SparkConf().setAppName(name).setMaster("local[*]"))
      , Seconds(time))
    ssc
  }
}
