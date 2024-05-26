import com.esotericsoftware.kryo.Kryo;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.serializer.KryoRegistrator;
import scala.Tuple2;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;


public class SparkRDDDome implements KryoRegistrator {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("javaspark");
        conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
//        conf.registerKryoClasses(Array(,cl))
        JavaSparkContext javaspark = new JavaSparkContext()

                ;
        javaspark.setLogLevel("WARN");

        JavaRDD<String> rdd = javaspark.textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt");

        JavaRDD<String> wordsRDD = rdd.flatMap(line -> Arrays.asList(line.split(" ")).iterator());

        JavaPairRDD<String, Integer> wordRDDAndOne = wordsRDD.mapToPair(word -> new Tuple2<>(word, 1));
        JavaPairRDD<String, Integer> wordAndCount = wordRDDAndOne.reduceByKey((a, b) -> a + b);

//        wordAndCount.foreach(word -> System.out.println(word));
        List<Tuple2<String, Integer>> collect = wordAndCount.collect();
        collect.forEach(System.out::println);  //方法引用

        javaspark.stop();
    }

    @Override
    public void registerClasses(Kryo kryo) {
        kryo.register(SparkRDDDome.class);
    }
}
