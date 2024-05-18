package study.p3_oop

object EnumerationAndApp {
  def main(args: Array[String]): Unit = {
    println(Color.RED)


    //Type 定义新类型
    type S = String
    var v: S = "abc"

    def test(): S = "xyz"
  }

}

// 枚举类
object Color extends Enumeration {
  val RED = Value(1, "red")
  val YELLOW = Value(2, "yellow")
  val BLUE = Value(3, "blue")
}


// 应用类
object Test20 extends App {
  println("xxxxxxxxxxx");
}