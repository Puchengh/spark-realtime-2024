package study.pre.classsstudy

/**
  * apply方法通常称为注入方法，在伴生对象做一些初始化操作
  * apply方法的参数列表  不需要和构造器的参数列表统一
  * 模式匹配调用unapply方法  unapply方法通常被称为提取方法，使用unapply方法来提取固定数量的对象
  * unapply方法会返回一个序列 （Option）  内部生成了一个Some对象来存放一些值
  * 不用new  直接调用类名一定是调用了apply方法    否则就需要new对象出来
  * apply和unapply方法会被隐式的调用
  * @param name
  * @param age
  * @param faceValue
  */
class ApplyDome(val name:String, var age: Int, var faceValue: Int) {

}


object ApplyDome{
  def apply(name: String,age: Int,faceValue: Int):
      ApplyDome = new ApplyDome(name,age,faceValue)
  def apply(name: String,age: Int,faceValue: Int,gender: String):
  ApplyDome = new ApplyDome(name,age,faceValue)

  def unapply(applyDome: ApplyDome): Option[(String, Int, Int)] = {

    if( applyDome == null){
      None
    }else{
      Some(applyDome.name,applyDome.age,applyDome.faceValue)
    }
  }

}

//单列对象
object Test2{
  def main(args: Array[String]): Unit = {
    val applyDome = ApplyDome("jingjing", 24, 90)   //调用apply方法
    val applyDome1 = ApplyDome("jingjing", 24, 90, "男")

    applyDome match {
      case ApplyDome("jingjing",age,faceValue) => println(s"age: $age")
      case _=> println("No match nothing")
    }
  }

}