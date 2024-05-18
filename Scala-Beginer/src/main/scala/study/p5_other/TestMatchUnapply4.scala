package study.p5_other

object TestMatchUnapply4 {
  def main(args: Array[String]): Unit = {
    val user: User = User("zhangsan", 11)
    val result = user match {
      case User("zhangsan", 11) => "yes"
      case _ => "no"
    }
    println(result)
  }
}


case class User(name: String,age: Int) //样例类


//class User(name: String,age: Int)

//object User {
//  def apply(name: String, age: Int): User = new User(name, age)
////必须实现一个unplay方法 用来对对象属性进行拆解
//  def unapply(user: User): Option[(String, Int)] = {
//    if (user == null)
//      None
//    else
//      Some(user.name, user.age)
//  }
//}