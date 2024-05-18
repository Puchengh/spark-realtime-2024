package study.p2_function

import scala.annotation.tailrec

object FunctionClosureCurrying {
  def main(args: Array[String]): Unit = {

    def add(a: Int, b: Int): Int = {
      a + b
    }

    //1、考虑固定一个加数的场景
    def addByFour(b: Int): Int = {
      4 + b
    }

    //2.考虑固定加数改变的场景
    def addByFive(b: Int): Int = {
      5 + b
    }

    //将固定加数作为另一个参数传入，但是作为"第一层参数"传入

    def addByFour1(): Int => Int = {
      val a = 4

      def addB(b: Int): Int = {
        a + b
      }

      addB
    }

    def addByA(a: Int): Int => Int = {
      def addB(b: Int): Int = {
        a + b
      }

      addB
    }

    println(addByA(36)(24))


    //lambda表达式简写
    def addByB(a: Int): Int => Int = a + _

    println(addByB(36)(24))


    //函数柯里化
    def addCurrying(a: Int)(b: Int): Int = {
      a + b
    }

    println(addCurrying(36)(24))


    println(test(5))

  }


  def test(i: Int): Int = {
    if (i == 1) {
      1
    } else {
      i * test(i - 1)
    }
  }


  //尾递归

  def tailFact(n:Int):Int = {
    @tailrec
    def loop(n:Int,currRes:Int):Int = {
      if (n == 0 )return currRes
      loop(n-1,currRes*n)
    }
    loop(n,1)
  }


}
