package study.pre.actor



//package com.actor

//import scala.actors.Actor
//
///**
//  * Actor是一个特质
//  */
//object ActorTest extends Actor{
//  override def act(): Unit = {
//    for (i <- 1 to 20 ){
//      println("actorTest:"+i)
//      Thread.sleep(1000)
//    }
//  }
//}
//
//
//object ActorTest2 extends Actor{
//  override def act(): Unit = {
//    for (i <- 1 to 20 ){
//      println("actorTest2:"+i)
//      Thread.sleep(1000)
//    }
//  }
//}
//
//object ActorTestTest{
//
//  def main(args: Array[String]): Unit = {
//    //启动actor  重写act方法
//    ActorTest.start()
//    ActorTest2.start()
//  }
//
//}