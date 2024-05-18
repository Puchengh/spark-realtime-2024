package study.p4_collection

import scala.collection.mutable

object HithLevelFunctionReduce {

  def main(args: Array[String]): Unit = {

    val list = List(1, 2, 3, 4)
    // 将数据两两结合，实现运算规则
    val i: Int = list.reduce((x, y) => x - y)
    println("i = " + i)
    println(list.reduce(_ + _))
    println(list.reduceLeft(_ + _))
    println(list.reduceRight(_ + _))

    println(list.reduceRight(_ - _)) // 1 - (2 - ( 3 - 4))
    // 从源码的角度，reduce 底层调用的其实就是 reduceLeft
    // val i1 = list.reduceLeft((x,y) => x-y)
    // ((4-3)-2-1) = -2
    val i2 = list.reduceRight((x, y) => x - y)
    println(i2)


    // fold 方法使用了函数柯里化，存在两个参数列表
    // 第一个参数列表为  ： 零值（初始值）
    // 第二个参数列表为： 简化规则
    // fold 底层其实为 foldLeft
    val b = list.fold(1)(_ - _)
    val a = list.foldLeft(1)((x, y) => x - y)
    val a1 = list.foldRight(10)((x, y) => x - y) //1 - (2 - ( 3 - (4-10))))
    println(b)
    println(a)
    println(a1)

    println("*"*100)
    // 两个 Map 的数据合并
    val map1 = mutable.Map("a" -> 1, "b" -> 2, "c" -> 3)
    val map2 = mutable.Map("a" -> 4, "b" -> 5, "d" -> 6)
    val map3: mutable.Map[String, Int] = map2.foldLeft(map1) {
      (map, kv) => {
        val k = kv._1
        val v = kv._2
        map(k) = map.getOrElse(k, 0) + v
        map
      }
    }
    println(map3)
    println(map1)
    println(map2)

  }

}
