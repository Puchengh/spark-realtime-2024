import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import scala.collection.mutable.HashMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SparkStreamDome {
    public static void main(String[] args) throws InterruptedException {
        JavaSparkContext javaspark = new JavaSparkContext(new SparkConf().setMaster("local[*]").setAppName("javaspark"));

        JavaStreamingContext stream = new JavaStreamingContext(javaspark, Durations.seconds(4));


        javaspark.setLogLevel("WARN");

        HashMap<Object, Object> map = new HashMap<>();

//        JavaInputDStream<ConsumerRecord<Object, Object>> consumer = KafkaUtils.createDirectStream(stream, LocationStrategies.PreferConsistent(), //位置策略
//                ConsumerStrategies.Subscribe((Collection<String>) Arrays.asList("qwerqwe").iterator(), (Map<String, Object>) map));

        JavaReceiverInputDStream<String> linesStream = stream.socketTextStream("192.168.31.59", 9999);
        JavaPairDStream<String, Integer> result = linesStream.flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a + b);

        result.print();

        stream.start();
        stream.awaitTermination();
    }

}
