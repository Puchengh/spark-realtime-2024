package study.p1_base

import java.io.{File, PrintWriter}
import scala.io.Source

object FileIo {
  def main(args: Array[String]): Unit = {

    //读取文件
    Source.fromFile("src/main/resources/test.txt").foreach(print)


    //写入文件
    var io = new PrintWriter(new File("src/main/resources/out.txt"))
    io.write("write scala from java write")
    io.close()

  }
}
