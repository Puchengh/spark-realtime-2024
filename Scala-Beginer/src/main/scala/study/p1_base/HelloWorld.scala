package study.p1_base

/**
 * 声明一个单例对象(伴生对象)
 */
object HelloWorld {

  /**
   * 方法 从外部直接可以调用执行的方法
   * @param args
   */
  def main(args: Array[String]): Unit = {
    println("helloWorld")
    System.out.println("helloworld")
  }
}
