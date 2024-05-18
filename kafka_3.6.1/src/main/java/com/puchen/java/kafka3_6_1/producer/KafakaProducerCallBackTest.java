package com.puchen.java.kafka3_6_1.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @ClassName: KafakaProducerTest
 * @Desc: TODO
 * @Author: puchen
 * @Date: 2024/4/20 21:16
 * @Version: 1.0
 **/
public class KafakaProducerCallBackTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //创建配置对象
        HashMap<String, Object> conf = new HashMap<>();
        conf.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        //对生产的数据KV进行序列化
        conf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        conf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

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

//            producer.send(record);
            //异步发送  异步发送是比较快的
            Future<RecordMetadata> send = producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    System.out.println("数据发送成功:" + recordMetadata);

                }
            });
            System.out.println("发送数据!");
            //同步发送   同步发送是已经安全的的
            send.get();
        }

        producer.close();
    }

}
