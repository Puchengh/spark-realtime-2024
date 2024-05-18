package study.p3_oop

object SingleTon {
  def main(args: Array[String]): Unit = {
    val student12 = Student12.getInstance()
    student12.prininfo()

    val student13 = Student12.getInstance()
    student13.prininfo()

    println(student12)
    println(student13)
  }

}


class Student12 private(val name: String, val age: Int) {
  def prininfo(): Unit = {

    println(s"$name---$age---${Student11.school}")
  }
}
//饿汉式的单例模式
//object Student12{
//
//  private val student:Student12 = new Student12("alices",28)
//  def getInstance():Student12 = student
//}

//懒汉式的单例模式

object Student12{

  private var student:Student12 = _
  def getInstance():Student12 = {
      if(student == null){
        student = new Student12("alices",28)
      }
    student
  }
}