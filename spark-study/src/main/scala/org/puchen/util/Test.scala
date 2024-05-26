
package org.puchen.util

import java.util.{Properties, Random}

object Test {

  def main(args: Array[String]): Unit = {

    val random = new Random()
    val strList: List[String] = List("success", "fail", "error")
    var id: Int = 0
    while (true) {
      // 随机生成数据
      val digitsss = (1 to 8).map(_ => random.nextInt(10).toString).mkString
      //      val digits = (1 to 10).map(_ => random.nextInt(10).toString).mkString
      //      val value = id.toString.mkString +","+ "message_" + random.nextLong()+","+ strList(random.nextInt(3))+",15"+digits+","+(random.nextInt(3)*1000).toString.mkString
      val value = random.nextInt(300).toString.mkString + "\t" + random.nextInt(1000).toString.mkString + "\t" +
        random.nextInt(5).toString.mkString + "\t" + "8" + digitsss
      println(value)
      id = id + 1
      // 可以添加延时，避免过于频繁发送
      Thread.sleep(100)
    }

  }
}
