package study.p5_other

object PartialFunction5 {

  def main(args: Array[String]): Unit = {
    val tuples: List[(String, Int)] = List(("kafka", 1), ("Scala", 4), ("Hello", 5), ("spark", 2))

    val tuples1: List[(String, Int)] = tuples.map(tuple => (tuple._1, tuple._2 * 2))


    val tuples2: List[(String, Int)] = tuples.map(tuple => tuple match {
      case (k, v) => (k, v * 2)
    })
    val tuples3: List[(String, Int)] = tuples.map {
      case (k, v) => (k, v * 2)
    }

    println(tuples1)
    println(tuples2)
    println(tuples3)


    //偏函数的应用
    val positiveAbs: PartialFunction[Int, Int] = {
      case x if x > 0 => x
    }

    val negativeAbs: PartialFunction[Int, Int] = {
      case x if x < 0 => -x
    }

    val zeroAbs: PartialFunction[Int, Int] = {
      case 0 => 0
    }

    def abs(x: Int): Int = (positiveAbs orElse negativeAbs orElse zeroAbs)(x)

    println(abs(-45))
  }

}
