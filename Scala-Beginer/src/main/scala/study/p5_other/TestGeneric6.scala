package study.p5_other

object TestGeneric6 {
  def main(args: Array[String]): Unit = {
    var s1:MyList[Child] = new MyList[SubChild]
    var s2:MyList1[SubChild] = new MyList1[Child]
  }
}

//泛型模板
//class MyList<T>{}
//不变
//class MyList[T]{}
//协变
class MyList[+T]{}
//逆变
class MyList1[-T]{}
class Parent{}
class Child extends Parent{}
class SubChild extends Child{}