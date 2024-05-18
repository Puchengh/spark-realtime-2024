package com.puchen.java.kafka3_6_1.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * @ClassName: KafkaConsumerTest
 * @Desc: TODO
 * @Author: puchen
 * @Date: 2024/4/20 21:32
 * @Version: 1.0
 **/
public class KafkaConsumerOffsetTest {

    public static void main(String[] args) {
        
        //创建配置对象
        HashMap<String, Object> conf = new HashMap<>();

        conf.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");

        //对生产的数据KV进行序列化
        conf.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        conf.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        conf.put(ConsumerConfig.GROUP_ID_CONFIG,"puchen1");
        conf.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        //创建消费者对象

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(conf);

        //订阅主题
        //从kafka的主题中获取数据
        consumer.subscribe(Collections.singletonList("test"));


        boolean flag = true;
                while (flag){
                    consumer.poll(Duration.ofMillis(100));
                    Set<TopicPartition> assignment = consumer.assignment();
                    if (assignment != null && !assignment.isEmpty()) {
                        for (TopicPartition topicPartition : assignment) {
                            if (topicPartition.topic().equals("test")) {
                                consumer.seek(topicPartition,2);
                                flag = false;
                            }
                        }
                    }

                }

        while (true){
            ConsumerRecords<String, String> datas = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> data : datas) {
                System.out.println(data);
            }

        }

        //LEO Log End Offset  = size + 1 = 0,1,2,3
        //LEO 目前是3


        //关闭消费者对象
//        consumer.close();
    }
}
