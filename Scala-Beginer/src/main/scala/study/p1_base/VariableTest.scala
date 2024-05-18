package study.p1_base

object VariableTest {
  def main(args: Array[String]): Unit = {
    var a:Int = 10
    //（1）声明变量时，类型可以省略，编译器自动推导，即类型推导
    var a1=10
    val b1=10
    //（2）类型确定后，就不能修改，说明 Scala 是强数据类型语言。
    var a2=15  //a2类型是15
    //（3）变量声明时，必须要有初始值
//    var a3:Int
    //（4）在声明/定义一个变量时，可以使用 var 或者 val 来修饰，var 修饰的变量可改变，
    a1 = 12

    println(a1)
    println(b1)

    var alice = new Student("alice", 20)
    alice = new Student("alice", 30)

//    val alice = new Student("alice", 20)
        //alice = new Student("alice", 30)
    alice = null
    val bob = new Student("bob", 20)
    bob.printInfo()

  }

}
