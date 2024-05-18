package study.p4_collection.list

object CollectionList {

  def main(args: Array[String]): Unit = {

    //不可变的list
    //创建list
    val ints = List(12, 25, 98)
    println(ints)

    val ints6 = 1 :: 2 :: 3 :: 4 :: Nil

    //访问和遍历
    println(ints(1))
    ints.foreach(println)

    //添加元素
    val ints1 = ints :+ 10  //后面添加一个元素
    val ints2 = 10 +: ints
    println(ints)
    println(ints1)
    println(ints2)

    val ints3 = ints2.::(51)
    println(ints3)

    val ints4 = Nil.::(51)
    println(ints4)

    val ints5 = 1 :: 2 :: 3 :: 4 :: Nil
    println(ints5)


    //合并列表
    val ints7 = ints4 :: ints5
    println(ints7)

    val ints8 = ints4 ::: ints5
    println(ints8)

    val ints9 = ints4 ++ ints5
    println(ints9)

  }
}
