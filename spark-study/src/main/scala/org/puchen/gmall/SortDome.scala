package org.puchen.gmall

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object SortDome {

  /**
   * sortBy
   * sortByKey
   * top
   * @param args
   */
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = new SparkContext(new SparkConf().setAppName("spark").setMaster("local[*]"))

    sc.setLogLevel("WARN")

    val lines: RDD[String] = sc.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt")

    val rdd: RDD[(String, Int)] = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)

    val rdd2: RDD[(String, Int)] = rdd.sortBy(_._2, false)  //默认是true 按照升序


    val rdd3: RDD[(Int, String)] = rdd.map(_.swap).sortByKey(false)


    val rdd4: Array[(String, Int)] = rdd.top(3)(Ordering.by(_._2))  //函数的柯理化

    rdd2.take(3).foreach(println)
    rdd3.take(3).foreach(println)
    rdd4.take(3).foreach(println)

  }
}
