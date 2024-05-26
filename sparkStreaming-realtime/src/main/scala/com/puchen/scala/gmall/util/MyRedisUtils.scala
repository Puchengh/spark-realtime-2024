package com.puchen.scala.gmall.util

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

object MyRedisUtils {

  var jedisPool: JedisPool = null

  def getJedisClient : Jedis = {
    if (jedisPool == null) {

      val host: String = MyPropsUtils(MyConfig.REDIS_HOST)
      val port: String = MyPropsUtils(MyConfig.REDIS_PORT)
      val value = new JedisPoolConfig()
      value.setMaxTotal(8) //最大连接数
      value.setMaxIdle(10) //最大空闲
      value.setMinIdle(0) //最小空闲
      value.setBlockWhenExhausted(true) //忙碌时是否等待
      value.setMaxWaitMillis(200) //忙碌时等待时长 毫秒
      value.setTestOnBorrow(true) //每次获得连接的进行测试

      jedisPool = new JedisPool(value, host, port.toInt,1000,"123456")
    }
    jedisPool.getResource
  }

  def main(args: Array[String]): Unit = {
    val client: Jedis = MyRedisUtils.getJedisClient
    println(client)
  }
}
