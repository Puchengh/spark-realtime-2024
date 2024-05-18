package study.p4_collection.map

object ImmutableMap {

  def main(args: Array[String]): Unit = {
    //创建Map
    val map1: Map[String, Int] = Map("a" -> 13, "b" -> 25, "hello" -> 3)
    println(map1)
    println(map1.getClass)

    //遍历元素
    map1.foreach(println)
    map1.foreach((kv: (String, Int)) => println(kv))

    //去map中所有的key 和value
    for (key <- map1.keys) {
      println(s"$key ----> ${map1.get(key)}")
    }

    println(map1("a"))
    println(map1.get("c"))
    println(map1.getOrElse("c", 0))
  }
}
