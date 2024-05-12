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

import java.util
import scala.collection.mutable

/**
 * kafaka的工具类 用于生产数据和消费数据
 */
object MyPropsUtils {

  //kafka 基于sparkstraming消费配置
  //  private val consumerConfig: mutable.Map[String, String] = mutable.Map(
  //    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "kafka.bootstrap.servers",
  //    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
  //    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
  //    ConsumerConfig.GROUP_ID_CONFIG -> "gmall",
  //    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest",
  //    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "true",
  //    //自动提交时间间隔
  //
  //  ) //offset提交  手动


  /**
   * 消费者的参数
   */

  private val consumerConfigs: mutable.Map[String, Object] = mutable.Map[String, Object](
    //kafka集群位置
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> MyKafkaUtils(MyConfig.KAFKA_BOOTSTRAPS_SERVERS),
    //kv正反序列器
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
    //groupid
    // offset 提交
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "true",
    //    ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG -> "5000"  //默认是5s 自动提交的时间间隔
    // offset 重置
    //What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server
    //超过7天会删除 offeset已经找不到了 earliest或者latest  重置到头和尾
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest"
  )


  /**
   * 指定 offsets 位置消费
   */
  def getKafkaDStram(ssc: StreamingContext, topic: String, groupId: String):
  InputDStream[ConsumerRecord[String, String]] = {
    consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)

    //订阅主题 消费者的参数 考虑offset问题  可以指定
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(topic), consumerConfigs))
    kafkaDStream
  }

  /**
   * 生产者对象
   */
  val producer: KafkaProducer[String, String] = createProducer()

  /**
   * 创建生产者对象
   */

  def createProducer(): KafkaProducer[String, String] = {
    val produceConfig = new Properties()
    //生产者配置类  ProduceConfig
    //kafka集群配置
    //acks  级别
    //batch.size  缓冲区的大小
    //linger.ms  时间
    //retries  重试的次数
    //    # 储存应答
    //    acks:默认是1
    //    # 请求超时时长
    //    request_timeout_ms:默认是30000ms
    //      # 重试次数 retries:默认值是0
    //    # 缓冲池大小 buffer_memory:默认是32MB
    //      # 批次大小 batch_size:默认16KB
    //    # 批次间隔时间 linger_ms:默认0
    produceConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, MyKafkaUtils(MyConfig.KAFKA_BOOTSTRAPS_SERVERS))
    produceConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    produceConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    produceConfig.put(ProducerConfig.ACKS_CONFIG, "all")
    //midengxing配置
    produceConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")

    val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](produceConfig)
    producer
  }

  /**
   * 生产数据
   * 默认按照粘性分区策略
   */
  def send(topic: String, msg: String): Unit = {
    producer.send(new ProducerRecord[String, String](topic, msg))
  }

  /**
   * 生产数据
   * 默认按照key进行分区
   */
  def send(topic: String, key: String, msg: String): Unit = {
    producer.send(new ProducerRecord[String, String](topic, key, msg))
  }

  /**
   * 关闭生产者对象
   */

  def close(): Unit = {
    if (producer != null) producer.close()
  }

}
