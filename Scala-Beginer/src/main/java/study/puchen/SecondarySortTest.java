package study.puchen;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

/**
 * @program: scalatest
 * @description:
 * @author: Puchen
 * @create: 2019-07-18 15:55
 */
public class SecondarySortTest {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setAppName("test");
        conf.setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        final JavaRDD<String> fileRDD = sc.textFile("F:\\test.txt");
//        JavaRDD<ScondarySortKey> secondaryKeyRDD = fileRDD.map(new Function<String, ScondarySortKey>() {
//            public ScondarySortKey call(String s) throws Exception {
//                String[] fields = s.split(",");
//                return new ScondarySortKey(Integer.valueOf(fields[0]),
//                        Integer.parseInt(fields[1]));
//            }
//        });

        JavaPairRDD<ScondarySortKey, String> scondarySortKeyStringJavaPairRDD = fileRDD.mapToPair(new PairFunction<String, ScondarySortKey, String>() {
            public Tuple2<ScondarySortKey, String> call(String s) throws Exception {
                String[] fields = s.split(",");
                ScondarySortKey scondarySortKey = new ScondarySortKey(Integer.valueOf(fields[0]), Integer.parseInt(fields[1]));
                return new Tuple2<ScondarySortKey, String>(scondarySortKey, s);
            }
        });
        scondarySortKeyStringJavaPairRDD.sortByKey()
                .foreach(new VoidFunction<Tuple2<ScondarySortKey, String>>() {
                    public void call(Tuple2<ScondarySortKey, String> t) throws Exception {
                        System.out.println(t._2());
                    }
                });
        sc.stop();
    }
}
