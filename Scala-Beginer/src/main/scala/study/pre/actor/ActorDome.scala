package study.pre.actor



//package com.actor
//
//import scala.actors.Actor
//
///**
//  * 用actor实现同步和异步的消息的发送和接收
//  */
//class ActorDome extends Actor{
//  override def act(): Unit = {
//    while (true){
//      //偏函数
//      receive{
//        case "start" => println("starting...")
//        case AsynMsg(id,msg) => {
//          println(s"id: $id,SyncMsg: $msg ")
//          Thread.sleep(2000)
//          sender ! ReplyMsg(5, "success")
//        }
//        case SyncMsg(id, msg) => {
//          println(s"id: $id,SyncMsg: $msg")
//          Thread.sleep(2000)
//          sender !? ReplyMsg()
//        }
//      }
//    }
//
//  }
//}
//
////异步的
//case class AsynMsg(val id: Int,val msg: String)
////同步的
//case class SyncMsg(val id: Int,val msg: String)
//case class ReplyMsg(val id: Int,val msg: String)