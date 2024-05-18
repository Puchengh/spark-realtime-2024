package study.p3_oop

object ClassForAccess {

}


class Person{

  private var idCard:String = "264564687"
  protected var name:String = "Alices"
  var sex:String = "female"
  protected[p3_oop] var age:Int = 18

  def printInfo():Unit={

    println(s"Person:$idCard$name$sex$age")
  }
}