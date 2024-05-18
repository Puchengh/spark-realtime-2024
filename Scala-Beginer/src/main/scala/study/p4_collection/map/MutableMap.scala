package study.p4_collection.map

import scala.collection.mutable

object MutableMap {

  def main(args: Array[String]): Unit = {
    //创建Map
    val map1: mutable.Map[String, Int] = mutable.Map("a" -> 13, "b" -> 25, "hello" -> 3)
    println(map1)
    println(map1.getClass)

    //遍历元素
    map1.put("c", 12)
    println(map1)

    map1 += (("e", 7))
    println(map1)

    map1.remove("c")
    println(map1)

    println(map1.getOrElse("e",0))
    println(map1("b"))

    //修改元素
    map1.put("c", 22)
    println(map1)
    map1.put("c", 88)
    println(map1)

    //元素的合并
    val map2: Map[String, Int] = Map("r" -> 13, "p" -> 25, "i" -> 3)
    map1 ++= map2
    println(map1)
    println(map2)

  }
}
