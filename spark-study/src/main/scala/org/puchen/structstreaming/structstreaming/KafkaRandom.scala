package org.puchen.structstreaming.structstreaming

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.{Properties, Random}

object KafkaRandom {

  def main(args: Array[String]): Unit = {
    // Kafka 配置
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    // 创建 KafkaProducer 实例
    val producer = new KafkaProducer[String, String](props) // 主题名称
    val topic = "your-topic"

    // 随机数据生成器
    val random = new Random()
    var id: Int = 0
    val strList: List[String] = List("success", "fail", "error")

    try {
      while (true) {
        // 随机生成数据
        val digits = (1 to 10).map(_ => random.nextInt(10).toString).mkString
        val value = id.toString.mkString + "," + "message_" + random.nextLong() + "," + strList(random.nextInt(3)) + ",15" + digits + "," + (random.nextInt(3) * 1000).toString.mkString

        // 发送数据到 Kafka
        val record = new ProducerRecord[String, String](topic, value)
        producer.send(record)

        id = id+1
        // 可以添加延时，避免过于频繁发送
        Thread.sleep(1000)
      }
    } finally {
      // 关闭 Kafka 生产者
      producer.close()
    }
  }
}
