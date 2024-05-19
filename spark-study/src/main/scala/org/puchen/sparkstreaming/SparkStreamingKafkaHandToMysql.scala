package org.puchen.sparkstreaming

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010._
import org.puchen.Util.SSC

object SparkStreamingKafkaHandToMysql {

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
      //      "auto.commit.interval.ms" -> "1000", //自动提交时间
      "enable.auto.commit" -> (false: java.lang.Boolean) //手动提交
    )

    val topic: String = "KafkaTopic"
    val kafakDS: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent, //位置策略
      ConsumerStrategies.Subscribe[String, String](Array(topic), kafkaParams) //消费策略
    )

    //处理消息
    //消费完成一批数据就提交查一次offset 而在DStream中一小批的体现是RDD

    kafakDS.foreachRDD(rdd => {

      if(!rdd.isEmpty()){
        rdd.foreach(record => {
          val topic: String = record.topic()
          val partition: Int = record.partition()
          val offset: Long = record.offset()
          val key: String = record.key()
          val value: String = record.value()

          val info: String = s"topic = ${topic},partition = ${partition},offset = ${offset},key = ${key},value = ${value}"
          println("消费到的信息为:" + info)

        })
        //获取rdd中offset相关的信息  offsetRanges里面包含了该批次各个分区的offset信息
        val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        //手动提交到mysql

        //这里代码省略  mysql的表四个字段  topic partition groupid offset

        println("当前批次的数据已经消费并且手动提交到mysql!!")
      }
    })

    ssc.start()
    ssc.awaitTermination()
    //优雅的关闭
    ssc.stop(stopSparkContext = true, stopGracefully = true)


  }

}
