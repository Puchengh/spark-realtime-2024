package study.p5_other

object ExceptionTest {

  //都输运行时的异常
  def main(args: Array[String]): Unit = {
    try {
      var n = 10 / 0
    } catch {
      case ex: ArithmeticException => {
        // 发生算术异常
        println("发生算术异常")
      }
      case ex: Exception => {
        // 对异常处理
        println("发生了异常 1")
        println("发生了异常 2")
      }
    } finally {
      println("finally")
    }

  }

  @throws(classOf[NumberFormatException])
  def f11()={ "abc".toInt }
}
