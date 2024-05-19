package org.puchen.sparkSQL

import org.apache.spark.SparkContext
import org.apache.spark.api.java.JavaRDD.fromRDD
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.streaming.StreamingContext
import org.puchen.Util.SSC
import org.puchen.dome.Person

object DF_DS_RDDDome {

  def main(args: Array[String]): Unit = {
    val ssc: SparkSession = SparkSession.builder().appName("spark").master("local[*]").getOrCreate()
    val sc: SparkContext = ssc.sparkContext
    val rdd: RDD[String] = sc.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt", 3)

    val jsonRDD: RDD[JsonDmone] = rdd.map(lines => {
      val arr: Array[String] = lines.split(" ")
      JsonDmone(arr(0), arr(1), arr(2))
    })

    import ssc.implicits._

    //rdd转df
    val rddDF: DataFrame = jsonRDD.toDF()
    //df转rdd
    val dfRDD: RDD[Row] = rddDF.rdd
    //rdd转ds
    val rddDS: Dataset[JsonDmone] = jsonRDD.toDS()
    //ds转rdd
    val dsRDD: RDD[JsonDmone] = rddDS.rdd
    //ds转df
    val dsDF: DataFrame = dsRDD.toDF()
    //df转ds
    val dfDS: Dataset[JsonDmone] = rddDF.as[JsonDmone]
  }

  case class JsonDmone(id:String,name:String,passs:String)
}
