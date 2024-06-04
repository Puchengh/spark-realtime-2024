package com.puchen.scala.gmall.test

import com.puchen.scala.gmall.util.MyRedisUtils
import redis.clients.jedis.Jedis

import java.text.SimpleDateFormat
import java.{lang, util}
import java.util.Date

object Test {

  def main(args: Array[String]): Unit = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyy-MM-dd")
    val date: Date = new Date(System.currentTimeMillis())
    val dasteStr: String = sdf.format(date)
    println(dasteStr)


    val isFlag: lang.Long = 1L
    println(isFlag == 1)
  }
}
