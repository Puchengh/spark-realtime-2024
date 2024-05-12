package com.puchen.scala.gmall.util

import java.util.ResourceBundle

object MyKafkaUtils {

  private val bundle: ResourceBundle = ResourceBundle.getBundle("config")
  def apply(prosKey:String):String = {
    bundle.getString(prosKey)
  }
//  def main(args: Array[String]): Unit = {
//    println(MyKafkaUtils.apply("kafka.broker.list"))
//    println(MyKafkaUtils("kafka.broker.list"))
//  }

}
