package study.p1_base

class Student(name: String, age: Int) {
  def printInfo(): Unit = {
    println(name + " " + age + " " + Student.school)
  }
}


//引入伴生对象
object Student {
  var school: String = "helan"

  def main(args: Array[String]): Unit = {
    val alice = new Student("alice", 20)
    val bob = new Student("bob", 23)
    alice.printInfo()
    bob.printInfo()

  }
}