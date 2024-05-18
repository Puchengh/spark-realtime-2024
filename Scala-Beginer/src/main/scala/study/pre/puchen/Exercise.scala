package study.pre.puchen

object Exercise {

  def main(args: Array[String]): Unit = {
    //创建一个list
    val list0 = List(2, 3, 7, 9, 4, 5, 8, 1, 6, 0)
    //将list0中每个元素乘以2以后生成一个新的集合
    val list1 = list0.map(_ * 2)
    //将list0的偶数取出来生成一个新的集合
    val list2 = list0.filter(_ % 2 == 0)
    //将list0排序后生成一个新的集合
    val list3 = list0.sorted
    //反转排序顺序
    val list4 = list3.reverse
    //将list0中的元素4个一组  类型问Iterator[List[Int]]
    val it = list0.grouped(4)
    println(it.toBuffer)
    //将iterator转换成list
    val list5 = it.toList
    println(list5)
    //将多个list压扁成一个list
    val list6 = list5.flatten
    println(list6)
    //先按空格切分,在压平
    val lines = List("hello java","hello scala","hello python")
//    val words = lines.map(_.split(" "))
//    val flattenWords = words.flatten
//    println(flattenWords)
    val res01 = lines.flatMap(_.split(""))
    println(res01)


    //并行计算求和
    val arr = Array(1,2,3,4,5,6,7,8,9,10)
//    val res02 = arr.sum
//    println(res02)
    //par 并行执行  和线程有关  每个线程计算一部分  平均分配线程计算
    val res02 = arr.par.sum
    println(res02)

    //按照特定的顺序来聚合
    //从左往右开始加数据
//    val re03 = arr.reduce(_+_)
//    val res03 = arr.reduceLeft(_+_)
    //并行计算  没有特定的顺序
    val res03 = arr.par.reduce(_+_)

    //折叠 有初始值（无特定顺序）  在运算的过程中加上初始值
    val res04 = arr.par.fold(0)(_+_)
    println(res04)

    //折叠 有初始值 (有特定的顺序)
    val res05 = arr.fold(10)(_+_)
    val res06 = arr.foldLeft(10)(_+_)
    val res07 = arr.foldRight(10)(_+_)
    //当在使用left right的时候   使用par不影响值
    val res08 = arr.par.foldLeft(10)(_+_)


    //聚合
    val list7 = List(List(1,2,3),List(4,5,6),List(2),List(0))
    val res09 = list7.flatten.reduce(_+_)
    println(res09)


    val res10 = list7.aggregate(0)(_+_.sum,_+_)   //第一个聚合是其中每个list的聚合 第二个参数是全局的聚合
    println(res10)

    var l1 = List(5,6,4,7)
    var l2 = List


    //求并集
//    val res11 = l1 union l2
    //求交集
//    val res = l1 insersect l2
    //求差集
//    val res l1 diff l2

  }

}
