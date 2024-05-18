package study.pre.currying

object Context{
  implicit  val a = "java"
  implicit  val b = "python"
}

object CurryingDome {

  def m1(str: String)(implicit name: String = "scala") = {
    println(str + name)
  }

  def main(args: Array[String]): Unit = {
//    val func = m1("Hi~") _
    import Context.a
    println(m1("Hi~"))
  }
}
