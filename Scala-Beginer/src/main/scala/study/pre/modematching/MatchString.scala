package study.pre.modematching

import scala.util.Random

/**
  * 匹配字符串
  */
object MatchString{
  def main(args: Array[String]): Unit = {
    val arr = Array("zhoudongyu","yangmi","liushishi","nini")
    val name = arr(Random.nextInt(arr.length))

    println(name)

    name match {
      case "nini" => println("倪妮")
      case "zhoudongyu" => println("周冬雨")
      case "yangmi" => println("杨幂")
      case "liushishi" => println("刘SS")
      case _ => println("Nothing")
    }
  }

}