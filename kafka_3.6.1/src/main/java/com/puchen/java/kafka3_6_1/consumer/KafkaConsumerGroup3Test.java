package com.puchen.java.kafka3_6_1.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;

/**
 * @ClassName: KafkaConsumerTest
 * @Desc: TODO
 * @Author: puchen
 * @Date: 2024/4/20 21:32
 * @Version: 1.0
 **/
public class KafkaConsumerGroup3Test {

    public static void main(String[] args) {
        
        HashMap<String, Object> conf = new HashMap<>();

        conf.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");

        //对生产的数据KV进行序列化
        conf.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        conf.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        conf.put(ConsumerConfig.GROUP_ID_CONFIG,"puchen");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(conf);

        consumer.subscribe(Collections.singletonList("test"));

        while (true){
            final ConsumerRecords<String, String> datas = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> data : datas) {
                System.out.println(data.partition());
            }
        }
    }
}
