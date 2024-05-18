package study.p4_collection.wordcount

object CollectionTuple {

  def main(args: Array[String]): Unit = {
    val tuple: (Int, String, Boolean) = (40, "bobo", true)
    println(tuple)

    //(2 )访问元组
    // (2.1 )通过元素的顺序进行访问，调用方式：_顺序号
    println(tuple._1)
    println(tuple._2)
    println(tuple._3)
    //(2.2)通过索引访问数据
    println(tuple.productElement(0))

    //(2.3)通过迭代器访问数据
    for (elem <- tuple.productIterator) println(elem)


    //(3)Map 中的键值对其实就是元组,只不过元组的元素个数为 2,称之为 对偶
    val map = Map("a" -> 1, "b" -> 2, "c" -> 3)
    val map1 = Map(("a", 1), ("b", 2), ("c", 3))
    map.foreach(tuple => {
      println(tuple._1 + "=" + tuple._2)
    })
    println(map1)


    //嵌套元素
    val mulTuple = (12, 0.3, "hello", (23, "scala", 79))
    println(mulTuple._4._2)
  }
}
