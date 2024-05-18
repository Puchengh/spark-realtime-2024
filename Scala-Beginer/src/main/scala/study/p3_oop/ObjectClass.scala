package study.p3_oop

object ObjectClass {
  def main(args: Array[String]): Unit = {
    //    val alices = new Student11("alices", 23)
    //    alices.prininfo()

    val alices = Student11.newStudent("alices", 26)
    alices.prininfo()

    val bob = Student11.apply("bob", 26)
    bob.prininfo()

    val tom = Student11("tom", 26)
    tom.prininfo()
  }
}

class Student11 private(val name: String, val age: Int) {
  def prininfo(): Unit = {

    println(s"$name---$age---${Student11.school}")
  }
}

object Student11 {
  val school: String = "shanxi"


  //定义一个类的对象实例的创建方法
  def newStudent(name: String, age: Int): Student11 = new Student11(name, age)

  def apply(name: String, age: Int): Student11 = new Student11(name, age)
}