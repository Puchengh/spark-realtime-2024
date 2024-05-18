package study.puchen;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * @program: scalatest
 * @description:
 * @author: Puchen
 * @create: 2019-07-17 10:36
 */
public class TransformationOperator {

    /**
     * 遍历rdd里面的数据
     */
    public static void map(){

        final SparkConf conf = new SparkConf().setAppName("map").setMaster("local");
        final JavaSparkContext sc = new JavaSparkContext(conf);

//        final List<String> list = Arrays.asList("周芷若", "赵敏", "小昭", "珠儿");
        final List<String> list = new ArrayList<String>();
        list.add("周芷若");
        list.add("赵敏");
        list.add("小昭");
        list.add("珠儿");

        JavaRDD<String> rdd = sc.parallelize(list);
        rdd.map(new Function<String, String>() {
            public String call(String name) throws Exception {
                return "hello, "+name;
            }
        }).foreach(new VoidFunction<String>() {
            public void call(String s) throws Exception {
                println(s);
            }
        });

        sc.stop();
    }

    public static void println(String str){
        System.out.println(str);
    }


    public  static void filter(){
        final SparkConf conf = new SparkConf().setAppName("filter").setMaster("local");
        final JavaSparkContext sc = new JavaSparkContext(conf);
        final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        final JavaRDD<Integer> parallelize = sc.parallelize(list);
        parallelize.filter(new Function<Integer, Boolean>() {
            public Boolean call(Integer integer) throws Exception {
                return integer % 2 == 0;
            }
        }).foreach(new VoidFunction<Integer>() {
            public void call(Integer integer) throws Exception {
                println(integer.toString());
            }
        });

    }


    public  static void flatMap(){
            final SparkConf conf = new SparkConf().setAppName("filter").setMaster("local");
            final JavaSparkContext sc = new JavaSparkContext(conf);
            final List<String> list = Arrays.asList("刘德华,张学友","黄渤,马老师");
            final JavaRDD<String> parallelize = sc.parallelize(list);
        parallelize.flatMap(new FlatMapFunction<String, String>() {
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(",")).iterator();
            }
        }).foreach(new VoidFunction<String>() {
            public void call(String s) throws Exception {
                println("sayHello  :   "+s);
            }
        });


    }


    public  static void groupByKey() {
        final SparkConf conf = new SparkConf().setAppName("filter").setMaster("local");
        final JavaSparkContext sc = new JavaSparkContext(conf);
        List<Tuple2<String, String>> list = Arrays.asList(
                new Tuple2<String, String>("武当", "张三丰"),
                new Tuple2<String, String>("峨眉", "灭绝师太"),
                new Tuple2<String, String>("武当", "宋青书"),
                new Tuple2<String, String>("峨眉", "周芷若"));
        final JavaRDD<Tuple2<String, String>> rdd = sc.parallelize(list);
        rdd.groupBy(new Function<Tuple2<String, String>, String>() {
            public String call(Tuple2<String, String> t) throws Exception {
                return t._1;
            }
        }).foreach(new VoidFunction<Tuple2<String, Iterable<Tuple2<String, String>>>>() {
            public void call(Tuple2<String, Iterable<Tuple2<String, String>>> t) throws Exception {
                println("门派" + t._1);
                Iterator<Tuple2<String, String>> iterator = t._2.iterator();
                while (iterator.hasNext()) {
                    final Tuple2<String, String> tuple = iterator.next();
                    println("人名： " + tuple._2);
                }
            }
        });
    }


    public static void reduceBuKey(){
        final SparkConf conf = new SparkConf().setAppName("filter").setMaster("local");
        final JavaSparkContext sc = new JavaSparkContext(conf);
        List<Tuple2<String, Integer>> list = Arrays.asList(
                new Tuple2<String, Integer>("少林寺", 40),
                new Tuple2<String, Integer>("丐帮", 50),
                new Tuple2<String, Integer>("少林寺", 50),
                new Tuple2<String, Integer>("丐帮", 70)
        );
        JavaPairRDD<String, Integer> rdd = sc.parallelizePairs(list);
        rdd.reduceByKey(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1;
            }
        }).foreach(new VoidFunction<Tuple2<String, Integer>>() {
            public void call(Tuple2<String, Integer> tuple) throws Exception {
                println(tuple._1 +"  "+tuple._2);
            }
        });

    }

    public static void sortByKey(){
        final SparkConf conf = new SparkConf().setAppName("filter").setMaster("local");
        final JavaSparkContext sc = new JavaSparkContext(conf);
        List<Tuple2<Integer, String>> list = Arrays.asList(
                new Tuple2<Integer, String>(90, "令狐冲"),
                new Tuple2<Integer, String>(80, "任我行"),
                new Tuple2<Integer, String>(99, "东方不败")
        );
        JavaPairRDD<Integer, String> rdd = sc.parallelizePairs(list);
        rdd.sortByKey(false).foreach(new VoidFunction<Tuple2<Integer, String>>() {
            public void call(Tuple2<Integer, String> tuple) throws Exception {
                println(tuple._1 +"  "+tuple._2);
            }
        });
    }


    public static void join() {
        final SparkConf conf = new SparkConf().setAppName("filter").setMaster("local");
        final JavaSparkContext sc = new JavaSparkContext(conf);
        List<Tuple2<Integer, String>> names = Arrays.asList(
                new Tuple2<Integer, String>(1, "黄老邪"),
                new Tuple2<Integer, String>(2, "郭靖"),
                new Tuple2<Integer, String>(3, "周伯通")
        );
        JavaPairRDD<Integer, String> nameRDD = sc.parallelizePairs(names);
        List<Tuple2<Integer, Integer>> scores = Arrays.asList(
                new Tuple2<Integer, Integer>(1, 90),
                new Tuple2<Integer, Integer>(2, 89),
                new Tuple2<Integer, Integer>(3, 99)
        );
        JavaPairRDD<Integer, Integer> scoreRDD = sc.parallelizePairs(scores);

        /**
         * Integer, Tuple2<String, Integer>
         *    学号           name     score
         */
        JavaPairRDD<Integer, Tuple2<String, Integer>> joinRDD = nameRDD.join(scoreRDD);
        /**
         * Integer, Tuple2<Integer, String>
         *     学号           score   name
         */
        JavaPairRDD<Integer, Tuple2<Integer, String>> join = scoreRDD.join(nameRDD);

        joinRDD.foreach(new VoidFunction<Tuple2<Integer, Tuple2<String, Integer>>>() {
            public void call(Tuple2<Integer, Tuple2<String, Integer>> integerTuple2Tuple2) throws Exception {
                println("学号："+integerTuple2Tuple2._1+"姓名："+integerTuple2Tuple2._2._1+"分数："+integerTuple2Tuple2._2._2);
            }
        });
    }


        public static void main(String[] args) {
//        map();
//        filter();
//        flatMap();
//        groupByKey();
          join();
    }
}
