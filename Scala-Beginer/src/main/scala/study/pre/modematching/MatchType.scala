package study.pre.modematching

import scala.util.Random

object MatchType {

  def main(args: Array[String]): Unit = {
    val arr = Array("abcde", 100, 3.14, true, MatchType)

    val element = arr(Random.nextInt(arr.length))

    println(element)

    element match{

      case str: String => println("match String： $Str")
      case int: Int => println("match String： $int")
      case bool: Boolean => println("match String： $bool")
      case matchTest: MatchTest => println("match String： $matchTest")
      case  _: Any  => println("Not matched")
    }
  }
}

class MatchTest{


}
