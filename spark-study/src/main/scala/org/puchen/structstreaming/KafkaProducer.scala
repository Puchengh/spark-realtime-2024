package org.puchen.structstreaming

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import java.util.{Properties, Random}

object KafkaProducer {

  def main(args: Array[String]): Unit = {

    val props: Properties = new Properties()

    props.put("bootstrap.servers", "192.168.31.59:9092")
    props.put("acks", "1")
    props.put("retries", "3")
    props.put("key.serializer", classOf[StringSerializer].getName)
    props.put("value.serializer", classOf[StringSerializer].getName)


    val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](props)

    val topic = "ods_produce_t"
    // 随机数据生成器
    val random = new Random()
    var id: Int = 0
    val strList: List[String] = List("success", "fail", "error","success","success","success")

    try {
      while (true) {
        // 随机生成数据
        val digits = (1 to 10).map(_ => random.nextInt(10).toString).mkString
        val value = id.toString.mkString + "," + "message_" + random.nextLong() + "," + strList(random.nextInt(6)) + ",15" + digits + "," + (random.nextInt(3) * 1000).toString.mkString
        println(value)
        // 发送数据到 Kafka
        val record = new ProducerRecord[String, String](topic, value)
        producer.send(record)

        id = id + 1
        // 可以添加延时，避免过于频繁发送
        Thread.sleep(100)
      }
    } finally {
      // 关闭 Kafka 生产者
      producer.close()
    }

  }
}
