package org.puchen.dome

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object PersistDome {

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = new SparkContext(new SparkConf().setAppName("sparkDome").setAppName("local[*]"))

    val rdd: RDD[Int] = sc.parallelize(1 to 10)

    val rddPersit: rdd.type = rdd.persist(StorageLevel.MEMORY_AND_DISK)  //只做了一个标记  真正的缓存实在action之后才会触发 不触发不会执行
    val rdd1: rdd.type = rdd.cache()  //默认就是StorageLevel.MEMORY_ONLY
    println(rddPersit.collect().mkString(","))
  }

}
