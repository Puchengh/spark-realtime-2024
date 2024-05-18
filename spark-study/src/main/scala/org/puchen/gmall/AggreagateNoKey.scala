package org.puchen.gmall

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object AggreagateNoKey {

  val sc: SparkContext = new SparkContext(new SparkConf().setAppName("Spark").setMaster("local[*]"))
  sc.setLogLevel("WARN")

  private val values: RDD[Int] = sc.parallelize(1 to 10)

  //下面这些都是action算子
  private val sumRdd: Double = values.sum()
  private val rddReduce: Int = values.reduce(_ + _)
  private val rddFold: Int = values.fold(0)(_ + _)
  private val rddAgg: Int = values.aggregate(0)(_ + _, _ + _)

  println(sumRdd)
  println(rddReduce)
  println(rddFold)
  println(rddAgg)

}
