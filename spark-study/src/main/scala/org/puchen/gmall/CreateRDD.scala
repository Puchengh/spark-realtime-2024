package org.puchen.gmall

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CreateRDD {

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = new SparkContext(new SparkConf().setAppName("Spark").setMaster("local[*]"))
    sc.setLogLevel("WARN")

    val rdd1: RDD[Int] = sc.parallelize(1 to 10)
    val rdd2: RDD[Int] = sc.parallelize(1 to 10,3)

    val rdd3: RDD[Int] = sc.makeRDD(1 to 10)
    val rdd4: RDD[Int] = sc.makeRDD(1 to 10,3)

    val rdd5: RDD[String] = sc.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt", 3)

    val rdd6: RDD[(String, String)] = sc.wholeTextFiles("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input")

    println(rdd1.getNumPartitions)
    println(rdd1.partitions.length)
  }
}
