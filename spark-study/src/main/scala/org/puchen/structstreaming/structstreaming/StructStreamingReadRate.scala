package org.puchen.structstreaming.structstreaming

import org.apache.spark.sql.{DataFrame, SparkSession}

object StructStreamingReadRate {

  def main(args: Array[String]): Unit = {
    val ssc: SparkSession = SparkSession.builder().master("local[*]").appName("spark")
      .config("spark.sql.shuffle.partitions", 4)
      .getOrCreate()

    val df: DataFrame = ssc.readStream.format("rate")
      .option("rowsPerSecond", "5") //每秒生成数据条数
      .option("rampUpTime", "0s") //每条数据生成间隔时间
      .option("numPartitions", "2") //分区数据
      .load()

    df.writeStream.format("console")
      .outputMode("append")
      .option("truncate",false)
      .start()
      .awaitTermination()
  }

}
