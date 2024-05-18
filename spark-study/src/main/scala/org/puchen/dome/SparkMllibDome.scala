package org.puchen.dome

import org.apache.parquet.format.IntType
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types.{DoubleType, IntegerType, LongType, StringType, StructField, StructType}
import org.apache.spark.ml.linalg.Vector

import scala.collection.parallel.immutable

object SparkMllibDome {

  def main(args: Array[String]): Unit = {

    /**
     * 查找出所有包含"spark的句子 即将包含"spark"的句子的标签设置为1 没有"spark"的句子的标签设置为0
     */
    val ssc: SparkSession = SparkSession.builder().master("local").appName("sparkMllib").getOrCreate()
    // 创建RDD
    val data = Seq(
      Row(0L, "spark", 1.0),
      Row(1L, "d c", 0.0),
      Row(2L, "spark f g h", 1.0),
      Row(3L, "hello hadoop", 0.0)
    )

    // 定义Schema
    val schema = StructType(
      List(
        StructField("id", LongType),
        StructField("text", StringType),
        StructField("label", DoubleType)
      )
    )

    // 将RDD转换为DataFrame
    val training = ssc.createDataFrame(ssc.sparkContext.parallelize(data), schema)

    //定义Pipeline中各个工作流阶段PipelineStage  包括装换器和评估器 具体的包含tokenizer hashingTF lr
    val tokenizer: Tokenizer = new Tokenizer().setInputCol("text").setOutputCol("words")
    val hashingTF: HashingTF = new HashingTF().setNumFeatures(1000).setInputCol(tokenizer.getOutputCol).setOutputCol("features")

    val lr: LogisticRegression = new LogisticRegression().setMaxIter(10).setRegParam(0.01)

    //创建pipeline
    val pipeline: Pipeline = new Pipeline().setStages(Array(tokenizer, hashingTF, lr))

    //现在是构建pipeline本质是一个Rstimator  在他的fit()方法运行之后 他将产生一个PipelineModel 他是一个TranFormer
    val model: PipelineModel = pipeline.fit(training)

    //测试数据集

    val testData = Seq(
      Row(4L, "spark as as "),
      Row(5L, "spark"),
      Row(6L, "asd asdf spark"),
      Row(7L, "hello hadoop")
    )

    val testSchema = StructType(
      List(
        StructField("id", LongType),
        StructField("text", StringType)
      )
    )

    val test: DataFrame = ssc.createDataFrame(ssc.sparkContext.parallelize(testData), testSchema)
    model.transform(test).select("id", "text", "probability", "prediction").collect()
      .foreach { case Row(id: Long, text: String, prod: Vector, prediction: Double) => println(f"(${id},${text} --> prod=${prod},prediction=${prediction})") }
  }

}
