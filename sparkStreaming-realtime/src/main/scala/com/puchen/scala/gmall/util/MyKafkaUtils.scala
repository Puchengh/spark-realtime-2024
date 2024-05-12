package com.puchen.scala.gmall.util

import java.util.Properties
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.clients.producer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.TopicPartition
import org.apache.logging.log4j.util.PropertiesUtil
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

import scala.collection.mutable

/**
 * kafaka的工具类 用于生产数据和消费数据
 */
object MyKafkaUtils {

  //kafka 消费配置
  private val consumerConfig: mutable.Map[String, String] = mutable.Map(
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "kafka.bootstrap.servers",
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
    ConsumerConfig.GROUP_ID_CONFIG -> "gmall",
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest",
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "true",
    //自动提交时间间隔

  ) //offset提交  手动


  /**
   * 指定 offsets 位置消费
   */
  def getKafkaDStream(topic: String, ssc: StreamingContext, offsets: Map[TopicPartition, Long], groupId: String)
  : InputDStream[ConsumerRecord[String, String]] = {
    consumerConfig(ConsumerConfig.GROUP_ID_CONFIG) = groupId

    val dStream: InputDStream[ConsumerRecord[String, String]] =
      KafkaUtils.createDirectStream[String, String](ssc, LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe[String, String](Array(topic), consumerConfig, offsets))

    return dStream

  }
}
