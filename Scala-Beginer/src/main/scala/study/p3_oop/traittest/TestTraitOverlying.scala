package study.p3_oop.traittest

object TestTraitOverlying {

  //钻石问题的问题叠加
  def main(args: Array[String]): Unit = {
    println(new MyBall().describe())

     //结果是my ball is a blue-foot-ball
  }
}


trait Ball {
  def describe(): String = {
    "ball"
  }
}
trait Color extends Ball {
  override def describe(): String = {
    "blue-" + super.describe()
  }
}
trait Category extends Ball {
  override def describe(): String = {
    "foot-" + super.describe()
  }
}
class MyBall extends Category with Color {
  override def describe(): String = {
    "my ball is a " + super.describe()
  }
}