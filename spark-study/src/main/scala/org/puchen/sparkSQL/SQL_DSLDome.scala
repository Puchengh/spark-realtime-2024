package org.puchen.sparkSQL

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object SQL_DSLDome {


  def main(args: Array[String]): Unit = {

    val ssc: SparkSession = SparkSession.builder().appName("spark").master("local[*]").getOrCreate()
    val sc: SparkContext = ssc.sparkContext

    val rdd: RDD[String] = sc.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt", 3)
    val df: DataFrame = ssc.read.text("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt")
    val ds: Dataset[String] = ssc.read.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt")

    import ssc.implicits._
    //    df.flatMap(_.split(" "))  //df没有泛型不能flatMap
    val words: Dataset[String] = ds.flatMap(_.split(" "))


    /**
     * SQL
     */
    words.createTempView("t_words")
    val sql:String =
      """
        |select value,count(1) as cnt
        |from t_words
        |group by value
        |order by cnt desc
        |""".stripMargin

    ssc.sql(sql).show()

    /**
     * DSL
     */
    words.groupBy('value)
      .count()
      .orderBy($"count".desc).show()

    words.printSchema()
    words.show()

    ssc.stop()
    sc.stop()

  }
}
