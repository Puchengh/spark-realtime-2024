package study.p5_other

//package com.puchen.study.p5_other
//
//object TestGeneric7 {
//  def main(args: Array[String]): Unit = {
//    test(classOf[SubChild])
//    test[Child](new Parent)
//  }
//  //泛型通配符之上限
//  def test[A <: Child](a:Class[A]): Unit ={
//   println(a.getClass.getName)
//  }
//  //泛型通配符之下限
////  def test[A >: Child](a:Class[A]): Unit ={
////   println(a)
////  }
//  //泛型通配符之下限 形式扩展
//  def test[A >: Child](a:A): Unit ={
//    println(a.getClass.getName)
//  }
//}
