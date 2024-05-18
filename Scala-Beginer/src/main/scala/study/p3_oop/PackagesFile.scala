package study.p3_oop

  object Outer{
    var out:String = "out"

    def main(args: Array[String]): Unit = {
      println(Inner.in)
    }
  }



  object Inner {
        val in:String = "puchen"
        def main(args: Array[String]): Unit = {

          println(Outer.out)
        }
      }


