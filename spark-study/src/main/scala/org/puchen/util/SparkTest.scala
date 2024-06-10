package org.puchen.util

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SparkTest {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("wordcount").setMaster("local[*]")
    val context = new SparkContext(conf)

    //    val name: Logger = Logger.getLogger(SparkTest.getClass.getName)
    //    name.setLevel(Level.ERROR)
    //    context.setLogLevel("ERROR")
    //    println(context)
    val lines: RDD[String] = context.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt")
    val words: RDD[String] = lines.flatMap(x => x.split(" "))
    //    val word: RDD[(String, Int)] = words.map(x => (x, 1))
    val word: RDD[(String, Int)] = words.mapPartitions(iter => {
      iter.map((_, 1))
    }) //每个分区一个链接  消耗资源少
    //    val value: RDD[(String, Int)] = word.reduceByKey(_ + _).sortBy(_._2,false)
    val value: RDD[(String, Int)] = word.reduceByKey(_ + _).sortByKey()
    //    value.foreach(println)
    value.foreachPartition(iter => iter.foreach(println))
    //    Thread.sleep(1000*60)

//    println()

    context.stop()
  }
}
