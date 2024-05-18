package study.pre.puchen



//package com.puchen
//
///**
//  * scala中用Lazy定义的变量就做惰性变量,会实现延迟加载
//  * 惰性变量只能是不可变的变量，且只有在调用惰性变量时，
//  * 才回去实例化这个变量
//  */
//object ScalaLazy {
//
//
//}
//
//  //object是本对象的半生类   相当于静态方法
//object ScalaLazy1{
//  def init():Unit = {
//
//    println("Call init()")
//  }
//  def main(args: Array[String]): Unit = {
//    //没有用lazy修饰的返回值
//    var property = init()
//    print("after init()")
//    print(property)
//
//  }
//
//}
//
//
//object ScalaLazy2{
//  def init():Unit = {
//    println("Call init()")
//  }
//  def main(args: Array[String]): Unit = {
//    //用lazy修饰的返回值   先编译在执行
////    lazy var property = init()
////    print("after init()")
////    print(property)
//
//  }
//
//}
//
