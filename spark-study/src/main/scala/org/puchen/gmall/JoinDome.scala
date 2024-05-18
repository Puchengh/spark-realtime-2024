package org.puchen.gmall

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object JoinDome {

  //增加并行度 减少通信开销
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = new SparkContext(new SparkConf().setAppName("sparkDome").setMaster("local[*]"))

    val rdd1: RDD[(Int, String)] = sc.parallelize(
      Seq((1001, "zhangsna"), (1002, "lisi"), (1003, "wangwu"), (1004, "zhaoliu"))
    )

    val rdd2: RDD[(Int, String)] = sc.parallelize(
      Seq((1003, "技术部"), (1004, "销售部"))
    )

    val rdd3: RDD[(Int, (String, String))] = rdd1.join(rdd2)
    val rdd4: RDD[(Int, (String, Option[String]))] = rdd1.leftOuterJoin(rdd2)
    val rdd5: RDD[(Int, (Option[String], String))] = rdd1.rightOuterJoin(rdd2)

    rdd3.foreach(println)
    rdd4.foreach(println)
    rdd5.foreach(println)


  }
}
