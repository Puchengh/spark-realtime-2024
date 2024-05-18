package study.pre.classsstudy

class TraitDome {

  def main(args: Array[String]): Unit = {
    val hunman = new Human
    println(hunman.name)
    println(hunman.climb)
    println(hunman.fight)
  }

}

/**
  *特质  相当于java中的接口
  */
trait Flyable{
  //s声明一个没有值得字段
  val distance: Int

  //声明一个没有实现的方法
  def fight: String

  //声明一个实现的方法
  def fly: Unit = {
    println("I can fly")
  }

}

/**
  * 抽象类
  */
abstract class Animal{
//声明一个没有赋值的字段
  val name:String

//声明一个没有实现的方法
  def run(): String

//声明一个有实现的方法
  def climb: String = {
    "I can climb"
  }

}

/**
  * 可以不适用关键字override 可以实现多个特质
  */

//class Human extends Flyable  如果不使用抽象类 则不需要使用with  直接使用extends实现接口
class Human extends  Animal with Flyable{
  override val name: String = "张三"
//重写了抽象类 没有实现的方法
  override def run(): String = "I an fly"

  override def climb: String = "override climb"

  override val distance: Int = 1000
//实现了特质中没有实现的方法
  override def fight: String = "with bangzi"

//实现了特质中有实现的方法
  override def fly: Unit = println("override fly")
}
