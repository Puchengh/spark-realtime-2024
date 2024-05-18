package org.puchen.gmall

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Test {

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = new SparkContext(new SparkConf().setAppName("Spark").setMaster("local[*]"))

    val rdd: RDD[(String, Int)] = sc.parallelize(Array(("spark", 2), ("hadoop", 6), ("hadoop", 4), ("spark", 6)))

    rdd.mapValues(x => (x, 1)).reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2)).mapValues(x => x._1 / x._2).foreach(println)

  }
}
