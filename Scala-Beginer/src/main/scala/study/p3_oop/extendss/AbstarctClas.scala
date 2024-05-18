package study.p3_oop.extendss

object AbstarctClas {
  def main(args: Array[String]): Unit = {
    val student = new Teacher1()
    student.sleep()
    student.hello()

    val pserson: Person = new Person {

      override var age: Int = 29

      override def hello(): Unit = println("person Hello")
    }

    println(pserson.age)
    pserson.hello()


  }


}

abstract class Person {
  val name: String = "person"
  var age: Int

  def hello(): Unit

  def sleep(): Unit = {
    println("Person sleep")
  }
}

class Teacher1 extends Person {
  override val name: String = "teacher"
  var age = 19

  def hello(): Unit = {
    println("hello teacher")
  }

  override def sleep(): Unit = super.sleep()
}