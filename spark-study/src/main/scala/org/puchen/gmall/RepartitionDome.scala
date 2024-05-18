package org.puchen.gmall

import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object RepartitionDome {

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = new SparkContext(new SparkConf().setAppName("Spark").setMaster("local[*]"))
    sc.setLogLevel("WARN")

    val rdd: RDD[(Int, String)] = sc.parallelize(
      Seq((1001, "zhangsna"), (1002, "lisi"), (1003, "wangwu"), (1004, "zhaoliu"))
    )

    val rdd1: RDD[Int] = sc.parallelize(1 to 10)
    val rdd2: RDD[Int] = rdd1.repartition(9)
    val rdd3: RDD[Int] = rdd1.repartition(13)

    println(rdd1.getNumPartitions)
    println(rdd1.partitions.size)
    println(rdd2.getNumPartitions)
    println(rdd3.getNumPartitions)


    //自定义分区器,每个返回的key会写到%对应的分区中
    rdd.partitionBy(new Partitioner {
      override def numPartitions = 3

      override def getPartition(key: Any) = {
        key.toString.toInt % 3
      }
    }).map(_._1).saveAsTextFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\output\\result")
  }
}
