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
public class KafakaProducerTest {

    public static void main(String[] args) throws InterruptedException {
        //创建配置对象
        HashMap<String, Object> conf = new HashMap<>();
        conf.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");


        //对生产的数据KV进行序列化
        conf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        conf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        conf.put(ProducerConfig.BATCH_SIZE_CONFIG, 100);

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
        for (int i = 0; i < 1000; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("test", i%2, "key" + i, "value" + i);
            System.out.println(record.toString());
            producer.send(record);
            Thread.sleep(1000);
        }

        producer.close();
    }
    

}
