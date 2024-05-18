package study.p2_function


object MyWhile {

  def main(args: Array[String]): Unit = {

    //1.常规的循环
    var n = 10
    while (n >= 1) {
      println(n)
      n -= 1
    }

    //2.自定义的while循环
    var i: Int = 1
    myWhile(i <= 10)({
      println(i)
      i -= 1
    })
  }

  //用闭包实现一个函数 将代码块作为参数传入 递归调用
  def myWhile(condition: => Boolean)(op: => Unit): Unit = {
    //内层函数需要递归调用 参数就是循环体
    if (condition) {
      op
      myWhile(condition)(op)
    }

  }
}
