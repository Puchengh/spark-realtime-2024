package study.pre.modematching







/**
  * 匹配数组 元组 集合
//  */
//object MatchList {
//
//  def main(args: Array[String]): Unit = {
//    //匹配数组 只要第一个匹配到了 个数也匹配的上 则可以匹配成功   去过匹配成功则自动跳出匹配
////    val arr = Array(3,4,5,6)
////    arr match {
////      case Array(6, g, h, j) => println(s"case: $g,$h,$j")
////      case Array(_, x, y) => println(s"case: $x,$y")
////      case _  => println("Not Matched")
////    }
//
//    //匹配元组
////    val tup = (2,3,4)
////    tup match{
////      case(3,a,b) =>println(s"case: $a,$b")
////      case(_,x,y) =>println(s"case: $x,$y")
////      case _  => println("Not Matched")
////    }
//
//    //匹配集合  Nil一个空的list集合  属于list    ::-->list合并的集合
////    val list1 = List(0,1,2,3,4)
////    list1 match {
////      case List(0,a,b,c) =>println(s"case0: 0")
////      case 0 :: Nil =>println(s"case1: 0")
////      case a :: b :: c :: d :: Nil =>println(s"case2: $a,$b,$c,$d")
////      case 0 :: a => println(s"case3: $a")
////      case _ => println("No Matched")
////    }
////  }
//}
