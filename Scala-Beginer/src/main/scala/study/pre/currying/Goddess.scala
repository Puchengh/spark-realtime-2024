package study.pre.currying

object ImplicitContext{

  implicit object OderingGile extends Ordering[Gile]{
    override def compare(x: Gile, y: Gile): Int =
        if(x.faceValue>y.faceValue) 1 else -1
  }
}

class Gile(val name: String,var faceValue: Int){

  override def toString: String = s"name: $name,faceValue: $faceValue"
}

class Goddess[T: Ordering](val v1: T,val v2: T){
  def choose()(implicit ord: Ordering[T]) =
    if (ord.gt(v1,v2)) v1 else v2
}

object Goddess {
  def main(args: Array[String]): Unit = {

    import ImplicitContext.OderingGile
    val g1 = new Gile("nini",100)
    val g2 = new Gile("yangmi",98)
    val godess = new Goddess(g1,g2)
    val res = godess.choose()

    println(res.toString)

  }
}
