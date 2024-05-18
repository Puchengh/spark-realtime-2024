package study.p4_collection.array

object CollectionMulArray {

  def main(args: Array[String]): Unit = {
    val array: Array[Array[Int]] = Array.ofDim[Int](2, 3)
    array(0)(2) = 19
    array(1)(0) = 25

    println(array.mkString("-"))


      //多维数组遍历
    for (i <- 0 until array.length; j <- 0 until array(i).length) {
      println(array(i)(j))
    }

    for (i <- array.indices; j <- array(i).indices) {
      print(array(i)(j) + "\t")
      if (j == array(i).length - 1) println()
    }

    array.foreach(line => line.foreach(println))

  }


}
