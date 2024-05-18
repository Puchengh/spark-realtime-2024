package study.pre.modematching

/**
  * 偏函数 PartialFunction[A,B]  A是输入的类型  B是返回值的类型
  */
object PartialFunctionDome {
  //PartialFunction[String,Int]   String是参数的类型  Int是返回值类型
  def m1: PartialFunction[String,Int] = {
    case "one" => {
      println("case 1")
      1
    }
    case "two" => {
      println("case 2")
      2
    }
    case "three" => {
      println("case 3")
      3
    }
  }

  //这个偏函数的原生态的形式
  def m2(num:String):Int = num match {
    case "one" => 1
    case "two" => 2
    case "three" => 3
  }

  def main(args: Array[String]): Unit = {
    println(m1("one"))
    println(m2("two"))
  }
}
