package study.p3_oop

object AccessTest {
  def main(args: Array[String]): Unit = {

    val person = new Person()
//    person.idCard  //error
//    person.name   Worker  //error
    println(person.age)
    println(person.sex)
    person.printInfo()

    val worker = new Worker()
    worker.printInfo()

  }

}


class Worker extends Person{
  override def printInfo(): Unit = {
    name  = "bob"
    age = 25
    sex = "male"
    println(s"Worker:$name$sex$age")
  }
}