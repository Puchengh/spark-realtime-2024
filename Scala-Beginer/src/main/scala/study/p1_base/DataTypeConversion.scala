package study.p1_base

object DataTypeConversion {

  def main(args: Array[String]): Unit = {

    //自动类型转换
//    （1）自动提升原则：有多种类型的数据混合运算时，系统首先自动将所有数据转换成精度大的那种数据类型 ，然后再进行计算。

    val a1:Byte = 10
    val b1:Long = 2353L
    val result1:Long = a1 + b1
    val result2:Int = a1 + b1.toInt   //强转
//    （2）把精度大的数值类型赋值给精度小的数值类型时，就会报错，反之就会进行自动类型转换。

    val a2:Byte = 10
    val b2:Int = a2
    val c2:Byte = b2.toByte
//    （3）（byte ，short）和 char 之间不会相互自动转换。

    val a3:Byte = 10
    val b3:Char = 'b'
    val c3:Byte = b3.toByte
    println(c3)
//    （4）byte ，short ，char 他们三者可以计算，在计算时首先转换为 int 类型。

    val a4:Byte = 10
    val b4:Short = 20
    val c4:Char = 'a'
    val result4:Int = a4 + b4 + c4
    println(result4)


    //强制类型转换
    //（1）将数据由高精度转换为低精度，就需要使用到强制转换  字符串截取 不会四舍五入
    val n1:Int  = -2.5.toInt
    println(n1)

    //（2）强转符号只针对于最近的操作数有效，往往会使用小括号提升优先级
//    var n2:Int = 2.6.toInt+3.7.toInt
    var n2:Int = (2.6+3.7).toInt
    println(n2)




    //数值类型和 String 类型间转换
//    （1）基本类型转 String 类型（语法：将基本类型的值+""  即可）
    val q1:Int = 27
    val q2:String = "456789"
    println(q1+q2)

//    （2）String 类型转基本数值类型（语法：s1.toInt、s1.toFloat、s1.toDouble、s1.toByte、s1.toLong、s1.toShort）

    val m1:Int = "456789".toInt
    val m2:Float = "12.3".toFloat
    val m3:Int = "12.3".toFloat.toInt
    println(m3)


    var n:Int = 257
    var b:Byte = n.toByte

    println(b)
  }
}
