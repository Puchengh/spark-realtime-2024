package study.p1_base

import scala.util.control.Breaks
import scala.util.control.Breaks._



object CirculateOperation {
  def main(args: Array[String]): Unit = {
    //if分支
    //    println("请输入你的年龄:")
    //    val age:Int = StdIn.readInt()

    //    if (age >= 18) {
    //      println("成年!")
    //    }else{
    //      println("未成年!")
    //    }


    //    val result:String = if (age >= 18) {
    //      "成年!"
    //    }else{
    //      "未成年!"
    //    }
    //    println(result)

    //    val res:Any = if (age < 18)  "未成年!" else "成年!"
    //    println(res)


    //for循环

    //1.范围遍历
    for (i <- 1 to 5) println("宋宋,告别海狗人参丸吧" + i)

    for (i <- Range(1, 10)) println("宋宋，告别海狗人参丸吧" + i) //1-9

    for (i <- 1 until 5 + 1) println("宋宋，告别海狗人参丸吧" + i) //1-5

    //2.集合遍历

    for (i <- Array(12, 23, 45)) println(i)

    //3.循环守卫
    for (i <- 1 to 5 if i != 3) println(i + "宋宋")

    //4.步长
    for (i <- 1 to 10 by 2) println("i=" + i)



    //5.循环嵌套

    for (i <- 1 to 3; j <- 1 to 3) {
      println(" i =" + i + " j = " + j)
    }

    for (i <- 1 to 9) {
      for (j <- 1 to i) {
        print(s"$j * $i = ${j * i} \t")
      }
      println()
    }

    //简写
    for (i <- 1 to 9; j <- 1 to i) {
      print(s"$j * $i = ${j * i} \t")
      if (j == i) println()
    }


    //6.循环引入变量
    for (i <- 1 to 3; j = 4 - i) {
      println("i=" + i + " j=" + j)
    }


    //九层妖塔
    for (i <- 1 to 9) {
      val stars = 2 * i - 1
      val spaces = 9 - i
      println(" " * spaces + "*" * stars)
    }

    for (i <- 1 to 9;stars = 2 * i - 1;spaces = 9 - i) {
      println(" " * spaces + "*" * stars)
    }

    for(stars <- 1 to 17 by 2;spaces = (17-stars)/2)  println(" " * spaces + "*" * stars)


    //for循环默认值返回都是Unit
    val res = for (i <- 1 to 10) yield i * 2  //生成器
    println(res)

    //倒序
    for(i <- 1 to 10 reverse){
      println(i)
    }


    //while循环
//    var i = 0
//    while (i < 10) {
//      println("宋宋，喜欢海狗人参丸" + i)
//      i += 1
//    }


    var i = 0
    do {
      println("宋宋，喜欢海狗人参丸" + i)
      i += 1
    } while (i < 10)


    //循环中断
    try {
      for (elem <- 1 to 10) {
        println(elem)
        if (elem == 5) throw new RuntimeException
      }
    }catch {
      case e:Exception =>  //什么都不做
    }
    println("正常结束循环")

    println("采用 Scala 自带的函数，退出循环=====>")

    Breaks.breakable(
      for (elem <- 1 to 10) {
        println(elem)
        if (elem == 5) Breaks.break()
      }
    )
    println("正常结束循环")

    println("对 break 进行省略=====>")
    breakable {
      for (elem <- 1 to 10) {
        println(elem)
        if (elem == 5) break
      }
    }
    println("正常结束循环")

  }
}
