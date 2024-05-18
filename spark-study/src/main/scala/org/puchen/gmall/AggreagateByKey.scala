package org.puchen.gmall

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object AggreagateByKey {


  def main(args: Array[String]): Unit = {
    val sc: SparkContext = new SparkContext(new SparkConf().setAppName("Spark").setMaster("local[*]"))
    sc.setLogLevel("WARN")

    //手动设置分区的分数
    val rdd: RDD[String] = sc.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt", 2)

    val rddMap: RDD[(String, Int)] = rdd.flatMap(_.split(" ")).map((_, 1))
    //  val value: RDD[(String, Iterable[(String, Int)])] = rddMap.groupBy(_._1)

    //三种不同的方式
    //1.    val rdd6: RDD[(String, (String, Int))] = rddMap.groupByKey().mapValues(_.sum)
    //2.    val rdd7: RDD[(String, Int)] = rddMap.groupByKey().mapValues(_.sum)
    //3.    val rdd8: RDD[(String, Int)] = rddMap.groupByKey().map(t => (t._1, t._2.sum))

    val rddReduceByKey: RDD[(String, Int)] = rddMap.reduceByKey(_ + _)
    val rddFoldByKet: RDD[(String, Int)] = rddMap.foldByKey(0)(_ + _)
    val rddAggByKey: RDD[(String, Int)] = rddMap.aggregateByKey(0)(_ + _, _ + _)


    val value1: RDD[String] = rddAggByKey.map(t => t._1)
    val value2: RDD[String] = rddAggByKey.keys


    val value3: RDD[Int] = rddAggByKey.map(t => t._2)
    val value4: RDD[Int] = rddAggByKey.values



//    rddReduceByKey.foreach(println)
//    rddFoldByKet.foreach(println)
//    rddAggByKey.foreach(println)
    value1.foreach(println)
    value2.foreach(println)
    value3.foreach(println)
    value4.foreach(println)

  }

}
