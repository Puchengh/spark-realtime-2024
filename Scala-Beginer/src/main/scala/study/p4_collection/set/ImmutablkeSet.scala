package study.p4_collection.set

import java.util

object ImmutablkeSet {

  def main(args: Array[String]): Unit = {
    val set = Set(1, 2, 3, 4, 4)
    println(set)

    //添加元素
    val set1 = set + 20
    println(set)
    println(set1)

    //合并set
    val set3 = Set(3,4,5,6)
    val set4 = set1 ++ set3
    println(set1)
    println(set3)
    println(set4)

    //删除元素
    val set5 = set3 - 20
    println(set5)


    val factTables: util.Set[Int] =   new util.Set[Int]() {
      override def size() = ???

      override def isEmpty = ???

      override def contains(o: Any) = ???

      override def iterator() = ???

      override def toArray = ???

      override def toArray[T](a: Array[T]) = ???

      override def add(e: Int) = ???

      override def remove(o: Any) = ???

      override def containsAll(c: util.Collection[_]) = ???

      override def addAll(c: util.Collection[_ <: Int]) = ???

      override def retainAll(c: util.Collection[_]) = ???

      override def removeAll(c: util.Collection[_]) = ???

      override def clear() = ???
    }
  }
}
