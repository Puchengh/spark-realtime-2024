package study.p4_collection

object HithLevelFunctionMap {

  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val nestedList: List[List[Int]] = List(List(1, 2, 3), List(4, 5, 6), List(7, 8, 9))
    val wordList: List[String] = List("hello world", "hello spark", "hello scala")

    //(1)过滤
    println(list.filter(x => x % 2 == 0))
    println(list.filter(_ % 2 == 0)) //同上
    //(2)转化/映射
    println(list.map(x => x + 1))
    println(list.map(_ + 1))

    //(3)扁平化
    println(nestedList)
    println(nestedList(0) ::: nestedList(1)::: nestedList(2))
    println(nestedList.flatten)  //同上
    //(4)扁平化+映射 注：flatMap 相当于先进行 map 操作，在进行 flatten 操作
    println(wordList.flatMap(x => x.split(" ")))
    //(5)分组
    println(list.groupBy(x => x % 2))
    println(list.groupBy(_ % 2))

  }

}
