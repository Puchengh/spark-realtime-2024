package org.puchen.structstreaming.structstreaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}

object RDDSParkStreamStrunctStreaming {

  def main(args: Array[String]): Unit = {
    val sparkRdd: SparkContext = new SparkContext(new SparkConf().setMaster("lcoal[*]").setAppName("sparkTest"))
    val sparkStreaming: StreamingContext = new StreamingContext(new SparkContext(new SparkConf().setMaster("lcoal[*]").setAppName("sparkTest")), Seconds(10))
    val structStreaming: SparkSession = SparkSession.builder().master("local[*]").appName("StructStreaming").config("spark.sql.shuffle,partitions", "4").getOrCreate()



  }
}
