package study.p4_collection.list

import scala.collection.mutable.ListBuffer

object CollectionListBuffer {

  def main(args: Array[String]): Unit = {
    val list = new ListBuffer[Int]()
    val list2 = ListBuffer(12,15,16)

    println(list)
    println(list2)

    list.append(1)

    list2.append(10)
    list2.prepend(11)
    println(list2)

    list2.insert(1,9)
    println(list2)

    list2 +=25 += 11
    println(list2)

    20 +=: 30 +=: list2 +=25 += 11
    println(list2)

    //连接两个list
    var list3 = list ++ list2
    println(list3)

    list ++= list2
    println(list)
    println(list2)

    list ++=: list2
    println(list)
    println(list2)


    //修改元素
    list2(1) = 89
    list2.update(0,90)
    print(list2)

    //删除元素
    list2.remove(1)
    list2 -= 90
    println(list2)


  }

}
