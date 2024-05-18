package study.p2_function

object FunctionParameter {

  def main(args: Array[String]): Unit = {
    //    （1）可变参数
    def f2(str: String*): Unit = {
      println(str)
    }

    f2("alices")
    f2("2", "3", "4")

    //    （2）如果参数列表中存在多个参数，那么可变参数一般放置在最后
    def f3(str1: String, str: String*): Unit = {
      println(str)
    }

    f3("alices")
    f3("2", "3", "4")

    //    （3）参数默认值，一般将有默认值的参数放置在参数列表的后面
    def f4(str1: String = "puchen"): Unit = {
      println(str1)
    }

    f4()
    f4("alices")

    //    （4）带名参数
    def f5(str1: String, age: Int): Unit = {
      println(str1 + " " + age)
    }

    f5(str1 = "45", 45)
    f5(str1 = "45", age = 45)
  }

}
