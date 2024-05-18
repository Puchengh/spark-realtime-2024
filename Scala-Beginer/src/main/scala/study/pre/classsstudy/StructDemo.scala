package study.pre.classsstudy

/**
  * 主构造器的参数要放在类的后面  val修饰的构造参数具有不可变性 car修饰的构造参数具有可变性
  * 此时声明的facevalue：Int 之恩给你在本类中调用  伴生对象也无法调用
  * faceValue虽然没有用cal或者var修饰 默认是val
  * 辅助构造器的参数如果和主构造器不同  先要调用主构造器
  *
  * @param name
  * @param age
  * @param faceValue
  */
class StructDemo(val name: String,
                 var age:Int,faceValue: Int) {

  var gender: String  = _
//                  var age:Int,faceValue: Int = 90) {

  //主构造器
//  def getFaceValue(): Int = {
//    faceValue
//  }
  //辅助构造器
  def this(name: String,age: Int,faceValue: Int,gender: String){
  this(name,age,faceValue)    //辅助构造器第一行必须先调用主构造器
  this.gender = gender
}
}


object StructDemo{
  def main(args: Array[String]): Unit = {
    val s = new StructDemo("ningning",26,98)
//    val m = new StructDemo("ningning",26)
    val d = new StructDemo("taotao",26,98,"女")
//    val faceValue = m.getFaceValue()

//    s.name = "tingting"   //无法赋值因为是用val修饰的

    s.age = 90

    println(s.name)
    println(s.age)
//    println(s.faceValue)    //在类后面加属性的时候  如果不能使用var或val修饰  则不能访问
  }


}