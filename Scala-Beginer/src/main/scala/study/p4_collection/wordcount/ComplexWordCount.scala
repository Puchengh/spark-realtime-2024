package study.p4_collection.wordcount

object ComplexWordCount {

  def main(args: Array[String]): Unit = {
    // 单词计数：将集合中出现的相同的单词，进行计数，取计数排名前三的结 果
    val tuples: List[(String, Int)] = List(
      ("Hello Scala Hbase kafka", 1),
      ("Hello Scala Hbase", 4),
      ("Hello Scala", 5),
      ("Hello", 2))

    // 直接展开为普通版本
    val newStringList: List[String] = tuples.map(
      kv => {
        (kv._1.trim + " ") * kv._2
      }
    )
    println(newStringList)
    println(newStringList.flatMap(_.split(" ")))
    val tuples1: List[(String, Int)] = newStringList.flatMap(_.split(" "))
      .groupBy(word => word)
      .map(kv => (kv._1, kv._2.size))
      .toList
      .sortBy(_._2)(Ordering[Int].reverse)
      .take(3)
    println(tuples1)

    println("*"*100)
    //另外一种思路 基于与统计的结果进行转换
    val preCountList:List[(String,Int)] = tuples.flatMap(
      tuple => {
        val strings:Array[String] = tuple._1.split(" ")
        strings.map(word => (word,tuple._2))
      }
    )
    println(preCountList)
    val stringToTuples: Map[String, List[(String, Int)]] = preCountList.groupBy(kv => kv._1)
    println(stringToTuples)

    val map: Map[String, Int] = stringToTuples.mapValues(
      value => value.map(_._2).sum
    )
    val tuples2: List[(String, Int)] = map.toList.sortWith(_._2 > _._2).take(3)
    println(tuples2)






  }
}
