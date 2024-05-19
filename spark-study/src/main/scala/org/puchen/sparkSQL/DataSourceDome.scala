package org.puchen.sparkSQL

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import java.util.Properties

object DataSourceDome {

  def main(args: Array[String]): Unit = {

    val ssc: SparkSession = SparkSession.builder().appName("spark").master("local[*]").getOrCreate()
    val sc: SparkContext = ssc.sparkContext

    //读取两种方式
    //底层是 format("json").load(paths : _*)
    val df: DataFrame = ssc.read.json("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\test.json")
    val df1: DataFrame = ssc.read.format("json").load("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\test.json")


    df.coalesce(1).write.mode(SaveMode.Overwrite).json("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\result")
    df.coalesce(1).write.mode(SaveMode.Overwrite).csv("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\result")
    df.coalesce(1).write.mode(SaveMode.Overwrite).parquet("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\result")
    df.coalesce(1).write.mode(SaveMode.Overwrite).orc("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\result")

    //写入同上
    df.write.format("json").save("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\result")


    val prop: Properties = new Properties()
    prop.setProperty("user","root")
    prop.setProperty("password","123456")
    df.coalesce(1).write.mode(SaveMode.Overwrite).jdbc("jdbc:myqsl://local:3306/text?characterEncoding=UTF-8","t_person",prop)



    //用哪种方式写的可以用那种方式去读
    val readResult: Any = ssc.read.jdbc("jdbc:myqsl://local:3306/text?characterEncoding=UTF-8", "root", prop)


    ssc.stop()
    sc.stop()


  }
}
