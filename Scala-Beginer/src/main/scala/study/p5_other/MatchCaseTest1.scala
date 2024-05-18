package study.p5_other

object MatchCaseTest1 {

  def main(args: Array[String]): Unit = {

    //模式匹配
    var a: Int = 10
    var b: Int = 20
    var operator: Char = '-'
    var result = operator match {
      case '+' => a + b
      case '-' => a - b
      case '*' => a * b
      case '/' => a / b
      case _ => "illegal"
    }
    println(result)

    //模式守卫
    def abs(x: Int) = x match {
      case i: Int if i >= 0 => i
      case j: Int if j < 0 => -j
      case _ => "type illegal"
    }

    println(abs(-5))

  }

}
