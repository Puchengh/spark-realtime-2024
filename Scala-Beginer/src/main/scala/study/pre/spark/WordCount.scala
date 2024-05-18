package study.pre.spark

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object WordCount {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()

    //这个参数是用来之指定任务名  如果不配置就会报错
    conf.setAppName("wordcount")
    //如果不配置  默认是在集群上运行  如果想要在本地运行
    //那么配置成local模式即可
    conf.setMaster("local")

    val sc = new SparkContext(conf)

    val fileRDD: RDD[String] = sc.textFile("F:\\test.txt")

    val wordRDD: RDD[String] = fileRDD.flatMap( line => line.split("\t"))

    val wordOneRDD: RDD[(String,Int)] = wordRDD.map(word => (word,1))

    val wordCountRDD: RDD[(String,Int)] = wordOneRDD.reduceByKey(_+_)
    //只有rotByKey  然后把key value调换顺序
    val sortRDD: RDD[(String,Int)] = wordCountRDD.sortBy(_._2)
    sortRDD.foreach( t => {
      println("word:"+t._1+"---->"+"count:"+t._2)
    })
    sc.stop()
  }

}
