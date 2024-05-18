package study.pre.classsstudy

/**
  * private [calssstudy]  class PrivateDome {    声明包的访问权限
  * 构造器前面加上private是指伴生对象的权限，只有伴生对象才能访问
  * PrivateDome private (val gender: Int,var faceValue: Int) {
  */
class PrivateDome private (val gender: Int,var faceValue: Int) {

  //变量前加private 此时该字段称为私有字段
  private val name = "jingjing"

  //对象私有字段  表示只能在本类访问
  private[this] var age = 24

  //私有方法  只能在本类中访问
  private def sayHello():Unit = {
    println("jingjin's age is 24")
  }

}

object PrivateDome{

  def main(args: Array[String]): Unit = {
    val privateDome = new PrivateDome(0,90)
    privateDome.sayHello()
  }

}

object test3{
  def main(args: Array[String]): Unit = {

    //没有访问权限  因为是私有的  不能初始化对象
//    val privateDome1 = new PrivateDome(0, 90)
//    println(privateDome1.faceValue)
  }

}