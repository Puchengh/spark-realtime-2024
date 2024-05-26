package org.puchen.structstreaming

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object KafkaEtl {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local[*]").appName("StructStreaming").config("spark.sql.shuffle,partitions", "4").getOrCreate()
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("WARN")
    import spark.implicits._

    //从kafka读取数据
    val kafkadf: DataFrame = spark.readStream.format("kafka")
      .option("kafka.bootstrap.servers", "192.168.31.59:9092")
      .option("subscribe", "ods_produce_t")
      .load()
    val valueDS: Dataset[String] = kafkadf.selectExpr("CAST(value as STRING)").as[String]
    //过滤成功的人数据
    val etlResult: Dataset[String] = valueDS.filter(_.contains("success"))

    //加载到etl_sercess
    etlResult.writeStream.format("kafka")
      .option("kafka.bootstrap.servers", "192.168.31.59:9092")
      .option("topic","etl_sercess")
      .option("checkpointLocation","E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\output\\kafka")
      .start()
      .awaitTermination()

    spark.stop()

  }
}
