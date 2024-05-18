package study.pre.puchen

object ScalaWordCount {

  def main(args: Array[String]): Unit = {
    val lines = List("hello java hello python","hello scala",
      "hello scala hello java hello scala")

    //首先应该切分压平
    val words = lines.flatMap(_.split(" "))
    //把每个单词生成一个个的tuple
    val tuples = words.map((_, 1))
    //以key进行分组
    val groupby = tuples.groupBy(_._1)
    //统计单词的词频  统计value的长度
    val sumed = groupby.mapValues(_.size)

    //排序
    val cishu = sumed.toList.sortBy(_._2)
    //反转降序排列
    val reverse = cishu.reverse

    println(reverse)
  }

}
