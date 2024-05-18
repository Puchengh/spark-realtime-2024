package study.pre.puchen

import scala.collection.mutable._

object MapTest {
  //定义一个新的map
  val map1 = new HashMap[String,Int]()

  //map中添加参数
  map1("scala") = 1
  map1 += (("java",2))
  map1.put("c#",3)

  //map中移除数据
  map1 -= "java"
  map1.remove("C#")


}
