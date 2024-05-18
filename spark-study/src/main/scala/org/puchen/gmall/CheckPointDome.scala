package org.puchen.gmall

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CheckPointDome {

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = new SparkContext(new SparkConf().setAppName("Spark").setMaster("local[*]"))
    sc.setCheckpointDir("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input")
    val rdd: RDD[Int] = sc.parallelize(1 to 10)

    rdd.checkpoint()  //进行了序列化 放的就是rdd的结果  persist 保留了依赖关系  checkpoint 不保留依赖关系

    rdd.foreach(println)
  }

}
