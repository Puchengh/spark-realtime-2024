package com.puchen.java.kafka3_6_1.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;

/**
 * @ClassName: KafakaProducerTest
 * @Desc: TODO
 * @Author: puchen
 * @Date: 2024/4/20 21:16
 * @Version: 1.0
 **/
public class KafakaProducerIdemTest {

    public static void main(String[] args) {

        //创建配置对象
        HashMap<String, Object> conf = new HashMap<>();
        conf.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        //对生产的数据KV进行序列化
        conf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        conf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        开启幂等性操作的要求：  1.acks=-1  2.开始重试机制 3.在途请求缓冲区的数据不能大于5
        conf.put(ProducerConfig.ACKS_CONFIG, -1);  //默认应答方式为-1
        conf.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);   //开启幂等性 解决数据的重复和乱序操作
        conf.put(ProducerConfig.RETRIES_CONFIG, 5);   //重试次数
        conf.put(ProducerConfig.BATCH_SIZE_CONFIG, 5);   //批次大小
        conf.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 3000);  //请求超时时长


        //创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(conf);

        //创建数据
        //第一个参数:主题名称
        //第一个参数:数据的key
        //第一个参数:数据的value
//        ProducerRecord<String, String> record = new ProducerRecord<String, String>("test","key","value");
//
//        //通过生产者对象将数据发送到kafka
//
//        producer.send(record);


        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("test", "key" + i, "value" + i);

            producer.send(record);
        }

        producer.close();
    }

}
