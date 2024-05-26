package com.puchen.scala.gmall.util

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange
import redis.clients.jedis.Jedis

import java.util
import scala.collection.mutable

/**
 * offsets管理工具类  用于给redis中存储和读取offset
 * 管理方案:
 * 1.后置提交偏移量  -- 手动控制偏移量提交
 * 2.手动控制偏移量提交  -- SparkStreaming提供了手动提交方案 但是我们不能用 因为我们会对Dstream的结构进行转换
 * 3.手动提取偏移量维护搭配redis中
 * -> 从kafka中消费到数据 先提取偏移量
 * -> 等数据成功写出后 将偏移量存储到redis中
 * -> 从kafka直接拍每个消费数据之前 现将redis中读取偏移量  使用读取到的偏移量到kafka中消费数据
 */
object MyOffsetsUtils {

  /**
   * redis中存储offset
   * 问题:存储的offset从哪里来？  从消费的数据提取出来的，传入到该方法中  offsetRanges: Array[OffsetRange]
   * offset的结构是什么？
   * 1。kafka中offset中的结构  groupId+topic+partition(GTP) => offset
   * 2。从传入的offset中提取关键信息
   * 在redis中怎么存？
   * 类型 String list set zset hash(一个key一个value)
   * key: groupId+topic
   * value: partition-offset，partition-offset(多个)
   * 写入API: Hset/hmset
   * 读取API: hgetall
   * 是否过期: 不过期
   */

  def saveOffset(topic: String, groupid: String, offsetRanges: Array[OffsetRange]) = {

    if (offsetRanges != null && offsetRanges.length > 0) {
      //创建map
      val offsets: util.HashMap[String, String] = new util.HashMap[String, String]()
      for (offsetRange <- offsetRanges) {
        val partition: Int = offsetRange.partition
        val endOffset: Long = offsetRange.untilOffset //结束点
        offsets.put(partition.toString, endOffset.toString)
      }
      println(f"提交offset:${offsets}")
      //redis中存储
      val jedis: Jedis = MyRedisUtils.getJedisClient

      val redisKey: String = s"offsets:$topic:$groupid"
      jedis.hset(redisKey, offsets)
      jedis.close()
    }

  }

  /**
   * 从redis中读取存储的offset
   * 1.如果传递给sparkStraming 通过指定的offset进行消费
   * 2.sparkStraming要求的格式是什么？ Map[TopicPartition,Long]  主题分区+offset
   *
   */

  def reaOffset(topic: String, groupid: String): Map[TopicPartition, Long] = {
    val jedis: Jedis = MyRedisUtils.getJedisClient
    val redisKey: String = s"offsets:$topic:$groupid"
    val offsets: util.Map[String, String] = jedis.hgetAll(redisKey)
    println(f"读取到offset:${offsets}")

    val results: mutable.Map[TopicPartition, Long] = mutable.Map[TopicPartition, Long]()
    //将java的map转换为scla的map进行迭代
    import scala.collection.JavaConverters._
    for ((partitions, offset) <- offsets.asScala) {

      val tp: TopicPartition = new TopicPartition(topic, partitions.toInt)
      results.put(tp, offset.toLong)
    }
    jedis.close()
    results.toMap
  }
}
