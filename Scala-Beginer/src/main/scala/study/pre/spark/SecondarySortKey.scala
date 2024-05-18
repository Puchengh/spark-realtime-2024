package study.pre.spark

import org.apache.spark.{SparkConf, SparkContext}
import study.puchen.ScondarySortKey

class SecondarySortKey(val first:Int,val second:Int) extends Ordered[ScondarySortKey] with Serializable {
  override def compare(that: ScondarySortKey): Int = {

    if(this.first - that.getFirst != 0){
      return this.first - that.getFirst
    }else{
      return this.second - that.getSecond
    }
  }
}


object SecondarySortKey{

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("test").setMaster("local")
    val sc = new SparkContext(conf)
    sc.textFile("F:\\test.txt")
      .map(line => {
        (new SecondarySortKey(line.split(",")(0).toInt, line.split(",")(1).toInt), line)
      }).sortBy(_._2).foreach(tuple => println(tuple._2))

//  }).sortByKey().foreach(tuple => println(tuple._2))

    sc.stop()
  }
}
