package study.pre.puchen

import scala.collection.mutable._

object SeqTest {

  val list1 = List(1,2,3,4)
  //追加形成新的list
//  val list2 = 0 :: list1
//  val list3 = list1.::(1)
  val list4 = 0 +: list1
  val list5 = list1.+:(0)

  val list6 = list1 :+ 4
  //合并两个list
  val list7 = List(5,6,7)
  val list8 = list1 ++  list7
  val list9 = list1 ++: list7

//  val list1 = ListBuffer(1,2,3)
  //list1追加4这个值
//  list1 += 4

//  list1.append(5)

  val list2 = ListBuffer(6,7,8)
  //追加一个list
//  list1 ++= list2
  list1 ++ list2

  list1 :+ 4

}
