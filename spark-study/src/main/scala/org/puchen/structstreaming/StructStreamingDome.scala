package org.puchen.structstreaming

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.puchen.dome.Person
import shapeless.syntax.std.tuple.productTupleOps

object StructStreamingDome {

  def main(args: Array[String]): Unit = {
//    val ssc: SparkSession = SparkSession.builder().appName("spark").master("local[*]").getOrCreate()
//
//    val df: DataFrame = ssc.readStream
//      .format("socket")
//      .option("host", "localhost")
//      .option("port", "9999")
//      .load()
//
//    import ssc.implicits._
//    val ds: Dataset[Person] = df.as[Person]

  }
}
