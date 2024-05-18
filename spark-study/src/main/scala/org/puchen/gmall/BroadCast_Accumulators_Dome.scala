package org.puchen.gmall

import org.apache.commons.lang3.StringUtils
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator

import java.lang

object BroadCast_Accumulators_Dome {

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = new SparkContext(new SparkConf().setAppName("Spark").setMaster("local[*]"))
    val lines: RDD[String] = sc.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\wordsBrocast.txt")

    val mycounter: LongAccumulator = sc.longAccumulator("mycounter") //累加器

    val mylist: List[String] = List("!", "$", "%")

    val broadcastList: Broadcast[List[String]] = sc.broadcast(mylist) //共享变量


    //world的时候要统计特殊字符的数量
    val value: List[String] = broadcastList.value
    val rdd2: RDD[(String, Int)] = lines.filter(StringUtils.isNotBlank(_))
      .flatMap(_.split(" "))
      .filter(
        ch => if (value.contains(ch)) {
          mycounter.add(1L)
          false
        } else {
          true
        }
      ).map((_, 1)).reduceByKey(_ + _)

    rdd2.foreach(println)
    val result: lang.Long = mycounter.value
    print(f"特殊字符的数量:"+result)
  }
}
