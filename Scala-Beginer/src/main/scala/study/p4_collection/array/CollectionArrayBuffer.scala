package study.p4_collection.array

import scala.collection.mutable.ArrayBuffer

object CollectionArrayBuffer {

  def main(args: Array[String]): Unit = {
    //创建可变数据
    val ints:ArrayBuffer[Int] = new ArrayBuffer[Int]()
    val arr2 =ArrayBuffer(12,23,45)
    println(arr2.mkString("-"))
    println(arr2)

    //访问元素
    println(arr2(0))
    arr2(0) = 39
    println(arr2)

    //添加元素
    val arr = arr2 += 15
    println(arr2)
    println(arr)
    println(arr2==arr)   //true

    15 +=: arr2
    println(arr2)
    println(arr)


    //推荐使用方法名称的方式
    arr2.append(10)
    arr2.prepend(9)
    arr2.insert(1,8,10)
    println(arr2)

    arr2.insertAll(2,arr)
    arr2.prependAll(arr)


    //删除元素
    arr2.remove(3)  //删除索引
    println(arr2)

    arr2 -= 15   //只删除第一个元素
    println(arr2)

    println("*"*50)
    //可变数组转换为不可变数组
    val arr3 =ArrayBuffer(12,23,45)
    val array:Array[Int] = arr3.toArray

    println(array.mkString("-"))
    array.foreach(println)

    val buffer = array.toBuffer
    println(buffer)
    println(array)
  }
}
