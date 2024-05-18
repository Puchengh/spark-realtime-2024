package study.pre.classsstudy

/**
  * 与类名相同并且用object修饰的对象叫做伴生对象
  * 类和它的伴生对象之间可以相互访问私有方法和属性
  */
class Dog {
  private var name = "二哈"

  def printName(): Unit = {
    //在DOG类中 访问其伴生对象的私有属性
    println(Dog.ONGSTANT + name)
  }
  val list = List(1,2)

}

//伴生对象
object Dog{

  private val ONGSTANT = "汪汪汪："
  def main(args: Array[String]): Unit = {
    val  p = new Dog
    //访问类中的私有字段name
    println(p.name)

    p.name = "大黄"

    p.printName()
//    println(p.printName())
  }

}
