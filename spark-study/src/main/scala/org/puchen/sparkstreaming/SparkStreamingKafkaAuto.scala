package org.puchen.sparkstreaming

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.puchen.Util.SSC

object SparkStreamingKafkaAuto {

  /**
   * 自动提交偏移量到默认主题和checkpoint
   *
   * @param args
   */
  def main(args: Array[String]): Unit = {
    val ssc: StreamingContext = SSC.create("spark", 5)
    ssc.checkpoint("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\checkpoint")

    val kafkaParams: Map[String, Object] = Map[String, Object](
      "bootstrap.servers" -> "local:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "sparkDome",
      "auto.offset.reset" -> "latest", //没有的话从最新的开始提交
      "auto.commit.interval.ms" -> "1000", //自动提交时间
      "enable.auto.commit" -> (true: java.lang.Boolean)   //自动提交
    )

    val topic: String = "KafkaTopic"
    val kafakDS: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent, //位置策略
      ConsumerStrategies.Subscribe[String, String](Array(topic), kafkaParams) //消费策略
    )

    //处理消息
    val infoDS: DStream[String] = kafakDS.map(record => {

      val topic: String = record.topic()
      val partition: Int = record.partition()
      val offset: Long = record.offset()
      val key: String = record.key()
      val value: String = record.value()

      s"topic = ${topic},partition = ${partition},offset = ${offset},key = ${key},value = ${value}"
    })
    infoDS.print()

    ssc.start()
    ssc.awaitTermination()
    //优雅的关闭
    ssc.stop(stopSparkContext = true, stopGracefully = true)


  }

}
