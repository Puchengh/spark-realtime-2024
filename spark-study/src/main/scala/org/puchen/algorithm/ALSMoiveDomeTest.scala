package org.puchen.algorithm

import org.apache.spark.SparkContext
import org.apache.spark.ml.recommendation.{ALS, ALSModel}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object ALSMoiveDomeTest {


  def main(args: Array[String]): Unit = {

    //0.准备环境
    val spark: SparkSession = SparkSession.builder().config("spark.sql.shuffle.partitions", 4)
      .appName("spark")
      .master("local[*]")
      .getOrCreate()
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("WARN")

    import spark.implicits._
    import org.apache.spark.sql.functions._

    //1.加载数据
    val fileDS: Dataset[String] = spark.read.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\u.data")
    val ratingDSF: DataFrame = fileDS.map(line => {
      val arr: Array[String] = line.split("\t")
      (arr(0).toInt, arr(1).toInt, arr(2).toDouble)

    }).toDF("userId", "movieId", "score")

    val Array(trainSet,testSet) = ratingDSF.randomSplit(Array(0.2, 0.8))  //按照2划分训练集和测试集
    //2.构建ALS推荐算法模型并且训练

    val als = new ALS().setUserCol("userId")
      .setItemCol("movieId")
      .setRatingCol("score")
      .setRank(10)  //可以理解为Cm*n = Am*k x Bk*n k的值
      .setMaxIter(10) //最大迭代次数
      .setAlpha(1.0)  //迭代步长

    val model: ALSModel = als.fit(trainSet)

    //val testResult: DataFrame = model.recommendForUserSubset(testSet, 5)

    //计算模型误差
    //.....
    //3.给用户做推荐
    val result1: DataFrame = model.recommendForAllUsers(5)  //给所有用户推荐5部电影
    val result2: DataFrame = model.recommendForAllItems(5)  //给所有电影推荐5个用户
    val result3: DataFrame = model.recommendForUserSubset(sc.makeRDD(Array(191)).toDF("userId"), 5)  //给制定用户推荐五部电影
    val result4: DataFrame = model.recommendForItemSubset(sc.makeRDD(Array(536)).toDF("movieId"), 5)  //给制定电影推荐五个用户
    result1.show(false)
    result2.show(false)
    result3.show(false)
    result4.show(false)


  }
}
