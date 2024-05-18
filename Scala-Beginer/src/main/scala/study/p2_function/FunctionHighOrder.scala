package study.p2_function

object FunctionHighOrder {

  def main(args: Array[String]): Unit = {

    def f(n: Int): Int = {

      println("f调用")
      n + 1
    }

    val result: Int = f(123)
    println(result)
    //1.函数作为值进行传递
    val f1: Int => Int = f
    val f2 = f _
    println(f1)
    println(f1(12))
    println(f2)
    println(f2(13))

    //2.函数作为参数进行传递
    def dualEval(op: (Int, Int) => Int, a: Int, b: Int): Int = {
      op(a, b)
    }


    def add(a: Int, b: Int): Int = {
      a + b
    }

    println(dualEval(add, 12, 23))
    println(dualEval(_ + _, 12, 23))
    println(dualEval((a, b) => a + b, 12, 23))


    //3.函数作为函数的返回值返回

    def f5():Int => Unit = {

      def f6(a:Int):Unit={
        println("f6调用"+a)
      }

      f6   //将函数直接返回
    }
    val f6 = f5()

    println(f6)
    println(f5())
    println(f6(25))

    println(f5()(27))

  }

}
