package org.puchen.sparkSQL

import org.apache.spark.SparkContext
import org.apache.spark.sql.{Dataset, SparkSession}

object SparkOnHiveDome {


  def main(args: Array[String]): Unit = {
    val ssc: SparkSession = SparkSession.builder().appName("spark").master("local[*]")
      .config("spark.sql.shuffle.partitions",4)
      .config("spark.sql.warehouse.dir","hdfs://192.168.31.128/user/hive/warehouse")
      .config("hive.metastore.urls","thrift://192.168.31.128:9083")
      .enableHiveSupport()  //开启hive语法的支持
      .getOrCreate()
    val sc: SparkContext = ssc.sparkContext
    val ds: Dataset[String] = ssc.read.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt")

    ssc.sql("show create tbale t_tablename")

    ssc.stop()
    sc.stop()
  }
}
