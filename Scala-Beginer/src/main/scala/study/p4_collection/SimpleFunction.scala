package study.p4_collection

object SimpleFunction {

  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(1, 5, -3, 4, 2, -7, 6)

    println(list.sorted(Ordering[Int].reverse)) //默认都是升序
    //(1)求和
    println(list.sum)
    //(2)求乘积
    println(list.product)
    //(3)最大值
    println(list.max)
    //    println(list.maxBy(_._2))

    //(4)最小值
    println(list.min)
    //    println(list.minBy(_._2))
    //(5)排序
    // (5.1)按照元素大小排序
    println(list.sortBy(x => x)) //默认都是升序
    // (5.2)按照元素的绝对值大小排序
    println(list.sortBy(x => x.abs))

    // (5.3)按元素大小升序排序
    println(list.sortWith((x, y) => x < y)) //默认都是升序
    // (5.4)按元素大小降序排序
    println(list.sortWith((x, y) => x > y))


    println(list.sortWith(_ < _))
  }
}
