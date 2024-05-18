package org.puchen.dome

import org.apache.spark.sql.{SparkSession, Row}
import org.apache.spark.sql.types._


object DateFream_RDD {


  def main(args: Array[String]): Unit = {


    //由于toDF方法在Spark 2.0之后被弃用了，所以我们使用了createDataFrame方法来创建DataFrame。


    // 创建SparkSession
    val spark = SparkSession.builder()
      .appName("Create DataFrame with nulls using toDF")
      .master("local[*]")
      .getOrCreate()

    // 创建RDD
    val data = Seq(
      Row("Alice", 25, null),
      Row("Bob", 30, null),
      Row("Charlie", null, "Engineer")
    )

    // 定义Schema
    val schema = StructType(
      List(
        StructField("name", StringType, nullable = true),
        StructField("age", IntegerType, nullable = true),
        StructField("profession", StringType, nullable = true)
      )
    )

    // 将RDD转换为DataFrame
    val df = spark.createDataFrame(spark.sparkContext.parallelize(data), schema)

    // 显示DataFrame
    df.show()

  }
}
