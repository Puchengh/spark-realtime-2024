package study.pre.classsstudy

class Person {
  //用val修饰的属性  只有get方法   用var修饰的属性有get 和set方法
  val id: String = "100"
  var name: String = _

  //用private修饰的属性  该属性属于对象私有变量，只有本类才能访问  伴生对象可以访问到
  private var age: Int = _

  //private[this]修饰后 该属性属于对象私有变量  只有本类才能访问  伴生对象也访问不到
  private[this] val gender = "男"
}

object Person{

  def main(args: Array[String]): Unit = {
    val  p = new Person
    p.name = "puchen"
    p.age = 1000
//    println(p.id)    //用val属性只可读 不可修改
    println(p.name)   //null
    println(p.age)   //0
//    println(p.gender)   //访问不到
  }

}
object Test1{
  def main(args: Array[String]): Unit = {
    val p = new Person
//    println(p.age)   //无法访问
  }

}