package study.p5_other

object MatchTypes2 {
  def main(args: Array[String]): Unit = {

    //泛型擦除 底层只会判断到只是List,  Array是底层数据类型，不会存在泛型擦除
    println(describe(List(1, 2, 3, 4, 5)))
    //数组例外，可保留泛型
    println(describe(Array(1, 2, 3, 4, 5, 6)))
    println(describe(Array("abc")))


    for (arr <- Array(Array(0),
      Array(1, 0),
      Array(0, 1, 0),
      Array(1, 1, 0),
      Array(1, 1, 0, 1),
      Array("hello", 90))) { // 对 一个数组集合进行遍历
      val result = arr match {
        case Array(0) => "0" //匹配 Array(0) 这个数组
        case Array(x, y) => x + "," + y //匹配有两个元素的数 组，然后将将元素值赋给对应的 x,y
        case Array(0, _*) => "以 0 开头的数组" //匹配以 0 开头和数组
        case Array(x, 1, y) => "中间为0的三元数组" //匹配以 0 开头和数组
        case _ => "something else"
      }
      println("result = " + result)
    }

    println("*" * 100)
    //list 是一个存放 List 集合的数组
    //请思考，如果要匹配 List(88) 这样的只含有一个元素的列表,并原值返 回.应该怎么写
    for (list <- Array(List(0),
      List(1, 0),
      List(0, 0, 0),
      List(1, 0, 0),
      List(88))) {
      val result = list match {
        case List(0) => "0" //匹配 List(0)
        case List(x, y) => x + "," + y //匹配有两个元素的 List case List(0, _*) => "0 ..."
        case List(a) => "只有一个元素"
        case _ => "something else"
      }
      println(result)
    }


    val list: List[Int] = List(1, 2, 5, 6, 7)
    list match {
      case first :: second :: rest => println(first + "-" + second + "-" + rest)
      case _ => println("something else")
    }


    //对一个元组集合进行遍历
    for (tuple <- Array(
      (0, 1),
      (1, 0),
      (1, 1),
      (1, 0, 2))) {
      val result = tuple match {
        case (0, _) => "0 ..." //是第一个元素是 0 的元组
        case (y, 0) => "" + y + "0" // 匹配后一个元素是 0 的对偶元组
        case (a, b) => "" + a + "-" + b
        case _ => "something else" //默认
      }
      println(result)
    }

  }

  def describe(x: Any) = x match {
    case i: Int => "Int" + i
    case s: String => "String hello" + s
    case m: List[_] => "List" + m
    case c: Array[Int] => "Array[Int]" + c
    case a => "something else " + a
  }
}
