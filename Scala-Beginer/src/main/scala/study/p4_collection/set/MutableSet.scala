package study.p4_collection.set

import scala.collection.mutable

object MutableSet {
  def main(args: Array[String]): Unit = {
    val set = mutable.Set(1, 2, 3, 4, 5, 6)
    println(set)

    //添加元素
    val set2 = set + 11
    println(set2)
    set += 11
    println(set)

    val bool = set.add(10)
    println(bool)
    println(set)

    //删除元素
    set -= 11
    set.remove(10)
    println(set)

    //合并两个set

    val set3 = mutable.Set(5, 6,80,90)
    val set4  = set3 ++ set
    println(set4)

    println(set3)
    set3 ++= set
    println(set3)
  }

}
