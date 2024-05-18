package study.p2_function

object FunctionLazy {

  def main(args: Array[String]): Unit = {
    lazy val res = sum(10, 30)
    println("1.函数调用!")
    println("2.res=" + res)
    println("4.res=" + res)
  }
  def sum(n1: Int, n2: Int): Int = {
    println("3.sum 被执行。。。  ")
    return n1 + n2
  }
}
