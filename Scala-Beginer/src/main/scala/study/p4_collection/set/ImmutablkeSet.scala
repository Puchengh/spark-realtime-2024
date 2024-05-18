package study.p4_collection.set

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
  }
}
