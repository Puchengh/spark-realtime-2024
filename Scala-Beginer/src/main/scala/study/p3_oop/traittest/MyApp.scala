package study.p3_oop.traittest

class User(val name: String, val age: Int)

trait Dao {
  //向数据库插入数据
  def insert(user: User) = {
    println("insert into database :" + user.name)
  }
}

trait APP {
  //自身类型可实现依赖注入的功能
  _: Dao =>
  def login(user: User): Unit = {
    println("login :" + user.name)
    insert(user)
  }
}

object MyApp extends APP with Dao {
  def main(args: Array[String]): Unit = {
    login(new User("bobo", 11))
  }
}