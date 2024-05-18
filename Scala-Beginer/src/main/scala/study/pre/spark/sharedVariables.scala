package study.pre.spark

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel

object sharedVariables {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()

    //这个参数是用来之指定任务名  如果不配置就会报错
    conf.setAppName("wordcount")
    //如果不配置  默认是在集群上运行  如果想要在本地运行
    //那么配置成local模式即可
    conf.setMaster("local")

    val sc = new SparkContext(conf)

    val a=3;
    val array = Array(1,2,3,4,5)
    val rdd = sc.parallelize(array)

//    rdd.map(number => number * a).foreach(println)

    //把a设置成广播变量
    val broadcasta = sc.broadcast(a)
    rdd.map(number => number * broadcasta.value).foreach(println)


    //累加变量 默认为0 不能自己设置  //MapReduce   key,value  默认是也是long类型
    val accum = sc.longAccumulator("My Accunmulator")
    sc.parallelize(array).foreach(x => accum.add(x))
    println(accum.value)


    rdd.persist(StorageLevel.MEMORY_ONLY)


  }

}
