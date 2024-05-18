package study.pre.currying

import scala.io.Source

class RichFile(val file: String) {

  def read(): String = {

    Source.fromFile(file).mkString("")
  }
}

object RichFile{
  def main(args: Array[String]): Unit = {
    //这个过程是显式的实现了read的方法
//    val path = "F:\\commonfriend.txt"
//    var content: String = new RichFile(path).read()
//    println(content)
    //隐式的实现read方法（隐式的转换）

//      val path = "F:\\commonfriend.txt"
//      val content = path.read()
//      println(content)
  }
}
