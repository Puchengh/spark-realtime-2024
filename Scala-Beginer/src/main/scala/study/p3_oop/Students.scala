package study.p3_oop

import scala.beans.BeanProperty

object Student{

  def main(args: Array[String]): Unit = {
    var student = new Students()
    println(student.age)
    println(student.name)
    student.name = "username"
//    student.setAge(26)
    println(student.getName)
  }
}

class Students {

  private var username:String = "Alices"

  @BeanProperty
  var name:String = _

  @BeanProperty
  var age:Int = _


}
