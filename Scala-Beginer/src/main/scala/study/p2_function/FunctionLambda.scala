package study.p2_function

object FunctionLambda {

  def main(args: Array[String]): Unit = {
    val fun = (name:String)=> println(name)
//    fun("puchen")

    //定义一个函数 用函数作为参数输入

    def f(func:String => Unit):Unit={
      func("puchen")
    }
    f(fun)
    f((name:String)=> println(name))


    //匿名函数
//    （1）参数的类型可以省略，会根据形参进行自动的推导
    f((name)=> println(name))
//    （2）类型省略之后，发现只有一个参数，则圆括号可以省略；其他情况：没有参数和参 数超过 1 的永远不能省略圆括号。
    f(name=> println(name))
//    （3）匿名函数如果只有一行，则大括号也可以省略
    f(name=> println(name))
//    （4）如果参数只出现一次，则参数省略且后面参数可以用_代替
    f(println(_))

    f(println)


    //实际的实例

    def dualFunctionOneAndTeo(fun:(Int,Int)=>Int):Int={
      fun(1,2)
    }

    val add = (a:Int,b:Int) => a+b
    val sub = (a:Int,b:Int) => a-b
    println(dualFunctionOneAndTeo(add))
    println(dualFunctionOneAndTeo(sub))

    println(dualFunctionOneAndTeo((a,b) => a+b))
    println(dualFunctionOneAndTeo(_+_))
  }
}
