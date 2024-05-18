package org.puchen.dome

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object DateFrameDome {

  def main(args: Array[String]): Unit = {
    val ss: SparkSession = SparkSession.builder().master("local[*]").getOrCreate()
    val ds: Dataset[String] = ss.read.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt")
    val df: DataFrame = ss.read.json("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\test.json")
//    df.printSchema()
//    df.show()

//    ds.show()

//    df.printSchema()  //打印字段信息
    df.select(df("name"),df("url")+1).show()
    df.select(df("name").as("username")).show()
    df.filter(df("name")>"google").show()

    df.groupBy("name").count().show()

//    df.sort(df("age").desc).show()
//    df.show()

    ss.sql("select * from test")




  }


}
