package study.p1_base

object StringVariable {
  def main(args: Array[String]): Unit = {
    //（1）字符串，通过+号连接
    val name: String = "alice"
    var age: Int = 30
    println(name + "岁数" + age)

    println(name * 3)

    //（2）printf 用法：字符串，通过%传值。

    printf("%d岁的%s在学习", age, name)
    println()
    //（3）字符串模板（插值字符串）  ：通过$获取变量值
    println(s"${age}岁的${name}在学习")
    var num: Double = 2.36
    println(f"The num is ${num}%2.2f")
    println(raw"The num is ${num}%2.2f")
    val s =
      """
        |select
        |name
        |age
        |from user
        |where name = "zhangsan"
    """.stripMargin
    println(s)


    //如果需要对变量进行运算，那么可以加${}
    val s1 =
      s"""
         |select
         |   name,
         |    age
         |from user
         |where name="$name" and age=${age + 2}
      """.stripMargin
    println(s1)
    val s2 = s"name=$name"
    println(s2)


    val c1: Char = 'A'
    println(c1)

    val c2: Char = '9'
    println(c2)

    val c3: Char = '\t' //制表符
    val c4: Char = '\n' //换行符
    println(c3)
    println(c4)

    //转义字符
    println('\\')
    println('\"')
    //字符变量底层保存就是ASCII码
    val l1: Int = c1
    println(l1)

    val c7: Char = (l1 + 1).toChar
    println(c7)


    //空类型
    def m1(): Unit = {

      println("m1被调用执行!")
    }

    val a: Unit = m1()
    println(a)

    //空引用
    //    val v:Int = null  //error
    var student = new Student("alice", 20)
    student = null
    println(student)

    //Nothing
//    def m2(n: Int): Nothing = {
//      throw new Exception()
//    }
//
//    val value = m2(0)
//    println(value)


    def m3(n: Int): Int = {
      if (n == 0)
        throw new Exception()
      else
        return n
    }

    val value1 = m3(2)
    println(value1)

  }

}
