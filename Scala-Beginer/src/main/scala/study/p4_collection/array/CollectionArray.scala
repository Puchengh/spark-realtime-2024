package study.p4_collection.array

import scala.+:

object CollectionArray {
  def main(args: Array[String]): Unit = {

    //不可变
    //(1)数组定义
    val arr01 = new Array[Int](4)
    val arr2 = Array.apply(12, 23, 45, 6, 7, 87, 9)

    //所有的遍历方式
    for (i <- arr2.indices) println(i)
    for (elem <- arr2) println(elem)
    val iter = arr2.iterator
    while (iter.hasNext) println(iter.next())

    println(arr2.mkString("-"))

    println(arr01.length) // 4
    //(2)数组赋值
    //(2.1)修改某个元素的值
    arr01(3) = 10
    //(2.2)采用方法的形式给数组赋值
    arr01.update(0, 1)

    //(3)遍历数组
    //(3.1)查看数组
    println(arr01.mkString(","))
    //(3.2)普通遍历
    for (i <- arr01.indices) println(i)

    //(3.3)简化遍历
    def printx(elem: Int): Unit = {
      println(elem)
    }

    arr01.foreach(printx)
    // arr01.foreach((x)=>{println(x)})
    // arr01.foreach(println(_))
    arr01.foreach(println)

    //(4)增加元素（由于创建的是不可变数组，增加元素，其实是产生新的数 组）
    println(arr01)
    val ints1: Array[Int] = arr01.:+(5)
    val ints2: Array[Int] = arr01 :+ 5
    val ints3: Array[Int] = 20 +: arr01
    ints2.foreach(println)
    ints3.foreach(println)
    println(ints1)
  }
}
