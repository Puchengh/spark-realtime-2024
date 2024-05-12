package org.puchen.gmall

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCountOnYarn {

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      println("请输入input和output")
      System.exit(1) //0正常退出 其他表示非正常退出
    }

    val conf: SparkConf = new SparkConf().setAppName("spark-test") //.setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)

    sc.setLogLevel("WARN")

    val lines: RDD[String] = sc.textFile(args(0))

    val words: RDD[String] = lines.flatMap(_.split(" "))
    val wordAndOnes: RDD[(String, Int)] = words.map((_, 1))
    val result: RDD[(String, Int)] = wordAndOnes.reduceByKey(_ + _)
    result.foreach(println)
    println(result.collect().toBuffer)

    System.setProperty("HADOOP_USER_NAME", "root")

    result.repartition(1).saveAsTextFile(args(1))


    Thread.sleep(1000 * 60)

    sc.stop()
  }

}
