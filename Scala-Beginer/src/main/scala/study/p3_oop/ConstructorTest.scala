package study.p3_oop

object ConstructorTest {

  def main(args: Array[String]): Unit = {
//    val student = new Student1()
//    student.Student1()

//    val student1 = new Student1("alices")
    val student2 = new Student1("alices",25)
  }
}

//定义一个类
class Student1() {
  var name: String = _
  var age: Int = _
  println("1.主构造方法被调用!")

  def this(name: String) {
    this() //直接调用主构造器
    println("2.辅助构造方法被调用！")
    this.name = name
    println(s"name:$name age:$age")
  }


  def this(name: String, age: Int) {
    this(name)
    println("3.辅助构造方法被调用！")
    this.name = name
    println(s"name:$name age:$age")
  }

  def Student1():Unit={
    println("一般方法被调用！")
  }

}

//比较推荐这种方法
class Student2(name:String,age:Int)

//主构造器无参数
class Student3(name:String,age:Int){
  def printInfo(): Unit = {
    println(s"Worker:$name$age")
  }
}


class Student4(var name:String,var age:Int){
  var shool:String = _
  def this(name:String,age:Int,school:String){
    this(name,age)
    this.shool = school

    def printInfo(): Unit = {
      println(s"Worker:$name$age$school")
    }
  }


}