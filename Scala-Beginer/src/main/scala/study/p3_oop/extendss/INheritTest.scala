package study.p3_oop.extendss

object INheritTest {

  def main(args: Array[String]): Unit = {
    val student1: Student7 = new Student7("alices", 18)
    val studen2: Student7 = new Student7("alices", 18, "001")

    student1.printInfo()
    studen2.printInfo()

    val teacher = new Teacher()
    teacher.printInfo()

    def printInfo(person:Person7):Unit={
      person.printInfo()
    }

    println("=============")

    val person = new Person7()
    printInfo(student1)
    printInfo(teacher)
    printInfo(person)


    //java中 静态绑定属性  动态绑定方法

    //scala 属性和方法全部是动态绑定

  }
}

class Person7() {
  var name: String = _
  var age: Int = _

  println("1.父类的主构造器！")

  def this(name: String, age: Int) {
    this()
    println("2.父类的辅助构造器！")
    this.name = name
    this.age = age
  }

  def printInfo(): Unit = {
    println(s"Person7:$name$age")
  }
}

//定义子类
class Student7(name: String, age: Int) extends Person7 {
  var stdNo: String = _
  println("3.父类的主构造器！")

  def this(name: String, age: Int, stdNo: String) {
    this(name, age)
    println("4.子类的辅助构造器！")
    this.stdNo = stdNo
  }

  override def printInfo(): Unit = {

    println(s"Student7:$name$age$stdNo")
  }

}

class Teacher() extends Person7 {
  override def printInfo(): Unit = {
    println(s"Student7:$name$age")
  }
}


