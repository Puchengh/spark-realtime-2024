package org.puchen.sparkSQL

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.{Dataset, SparkSession}

object SparkSQLUDF {

  def main(args: Array[String]): Unit = {
    val ssc: SparkSession = SparkSession.builder().appName("spark").master("local[*]").getOrCreate()
    val sc: SparkContext = ssc.sparkContext
    val ds: Dataset[String] = ssc.read.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt")

    ds.printSchema()
    ds.show()

    import ssc.implicits._
    val wordDS: Dataset[String] = ds.flatMap(_.split(" "))
    wordDS.createOrReplaceTempView("t_word")

    /**
     * sql方式
     */
    //定义一个UDF函数
    ssc.udf.register("small2big",(value:String)=>{
      value.toUpperCase
    })

    //查询sql
    val sql:String =
      """
        |select value,small2big(value) as bigValue
        |from t_word
        |""".stripMargin
    ssc.sql(sql).show()


    /**
     * dsl语句
     */
    val small2big: UserDefinedFunction = udf((value: String) => {
      value.toUpperCase
    })
    wordDS.select('value,small2big('value).as("bigValue")).show()

    ssc.stop()


  }
}
