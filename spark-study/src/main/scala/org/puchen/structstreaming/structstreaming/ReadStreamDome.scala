package org.puchen.structstreaming.structstreaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext}


object ReadStreamDome {
  def main(args: Array[String]): Unit = {
    // SparkStreaming创建
    //    val context: StreamingContext = new StreamingContext(new SparkContext(new SparkConf().setAppName("spark").setMaster("local[*]")), Seconds(2))
    val ss: SparkSession = SparkSession.builder().config("spark.sql.shuffle.partitions", 4)
      .appName("spark")
      .master("local[*]")
      .getOrCreate()

    val df: DataFrame = ss.readStream.format("socket")
      .option("host", "10.107.105.8")
      .option("port", "9999")
      .load()

    import ss.implicits._
    val ds: Dataset[String] = df.as[String]

    val result: Dataset[Row] = ds.flatMap(_.split(" "))
      .groupBy("value")
      .count()
      .orderBy('count.desc)

    result.writeStream.format("console")
      .outputMode("complete")
      .start()
      .awaitTermination()

    ss.stop()
  }

}
