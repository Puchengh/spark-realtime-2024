package study.p2_function

object FunctionMethod {


  //方法可以重载重写  但是函数不行
  def main(args: Array[String]): Unit = {

    def sayHello(name: String): Unit = {
      println("hi," + name)
    }

    def f1():Unit = {
      println("F1调用")
    }

    f1()

    sayHello("alice")
    sayHi("Alice")

    //调用对象的方法
    FunctionMethod.sayHi("bob")


  }

  def sayHi(name: String): Unit = {
    println("hi," + name)
  }


  def sayHi(): Unit = {
    println("hi,")
  }

}
