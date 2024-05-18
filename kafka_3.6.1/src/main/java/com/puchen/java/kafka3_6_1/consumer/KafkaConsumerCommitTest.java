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
public class KafkaConsumerCommitTest {

    public static void main(String[] args) {
        
        //创建配置对象
        HashMap<String, Object> conf = new HashMap<>();

        conf.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");

        //对生产的数据KV进行序列化
        conf.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        conf.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        conf.put(ConsumerConfig.GROUP_ID_CONFIG,"puchen");

        conf.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        conf.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG,"read_committed");  //默认是read_uncommitted  不成功也能读取到数据

//        conf.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);  //默认是true

        //创建消费者对象

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(conf);

        //订阅主题
        //从kafka的主题中获取数据
        consumer.subscribe(Collections.singletonList("test"));

        while (true){
            final ConsumerRecords<String, String> datas = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> data : datas) {
                System.out.println(data);
            }
            //手动保存偏移量
//            consumer.commitAsync();   //异步提交
//            consumer.commitSync();   //同步提交
        }

        //LEO Log End Offset  = size + 1 = 0,1,2,3
        //LEO 目前是3


        //关闭消费者对象
//        consumer.close();
    }
}
