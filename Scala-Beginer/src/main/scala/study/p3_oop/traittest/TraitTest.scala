package study.p3_oop.traittest

object TraitTest {

  def main(args: Array[String]): Unit = {
    val student = new Student13
    student.sayHello()
    student.study()
    student.dating()
    student.play()

  }
}


class Person13 {
  val name: String = "pserson"
  var age: Int = 18

  def sayHello(): Unit = {
    println("hello from " + name)

  }
}

trait Young {
  var age: Int
  val name: String = "young"

  def play(): Unit = {
    println(s"young people $name is palying")
  }

  def dating(): Unit
}

class Student13 extends Person13 with Young{

  override val name: String = "student"

  override def dating(): Unit = println(s"student $name is dating")

  def study():Unit = println(s"student $name is studying")

  override def sayHello(): Unit = {
    super.sayHello()
    println(s"hello from :student $name")
  }
}