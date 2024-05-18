package study.p1_base

object Opertior {
  def main(args: Array[String]): Unit = {

    //数值运算
    val result1 = 10/3
    println(result1)

    val result2:Double = 10/3
    println(result2)

    val result3:Double = 10.0/3
    println(result3.formatted("%1.2f"))

    val result4:Int = 10%3
    println(result4)


  //2.比较运算符
    val s1:String  = "hello"
    val s2:String = new String("hello")
    println(s1 == s2)
    println(s1.equals(s2))
    println(s1.eq(s2))   //比较的是对象的地址

  //3.逻辑运算符
    println("#"*50)
    def m(n:Int):Int={
      print("m被调用")
      return n
    }
    val n = 1
    println((4 > 5) && m(n) > 0 )

    //判断字符串是否为空
    def isNotEmpty(str:String):Boolean={
      return str!=null && !("".equals(str.trim))
    }

    println(isNotEmpty(null))

    println("--"*50)
    //自增自减
    var b:Int = 10
    b += 1
    println(b)


    var n1 :Int =8
    n1 = n1 << 1
    println(n1)

    //运算符的方法调用
    val n2:Int = 13
    val n3:Int = 37
    println(n2.+(n3))
    println(n2 + n3)

    print(7.5 toString)

  }

}
