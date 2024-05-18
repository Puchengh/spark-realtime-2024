package study.p5_other

object ImplicitTest {

  def main(args: Array[String]): Unit = {

    //隐式类
    implicit class MYRichInt(val self:Int){
      def myMax(n:Int):Int = if (n < self) self else n
      def myMin(n:Int):Int = if (n < self) n else self
    }

    val int = new MYRichInt(12)
    println(int.myMax(15))
    //隐式函数

//    implicit def convert(num:Int):MYRichInt = new MYRichInt(num)
    println(12.myMax(15))
    println(12.myMin(15))


    //隐式参数  根据隐式的参数的类型去查找
    implicit val str: String = "hello world!"
    def hello(implicit arg: String="good bey world!"): Unit = {	 			println(arg)	 		}
    hello
  }



}

//implicit class MYRichInt(val self:Int){
//  def myMax(n:Int):Int = if (n < self) self else n
//  def myMin(n:Int):Int = if (n < self) n else self
//}
