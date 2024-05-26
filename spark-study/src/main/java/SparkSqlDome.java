import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.col;
import java.util.Arrays;

public class SparkSqlDome {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("123").master("local[*]").getOrCreate();
        spark.sparkContext().setLogLevel("WARN");

        Dataset<String> ds = spark.read().textFile("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt");

        Dataset<Row> dstext = spark.read().text("E:\\study\\spark-realtime-2024\\spark-study\\src\\main\\resources\\data\\input\\words.txt");

        Dataset<String> wordDS = ds.flatMap((String line) -> Arrays.asList(line.split(" ")).iterator(), Encoders.STRING());


        //SQL写法
        wordDS.createOrReplaceTempView("t_word");
        spark.sql("select value,count(*) as counts from t_word group by value order by counts desc limit 10").show();

        //DSL写法
//        Dataset<Row> temp = wordDS.groupBy("value")
//                .count();
//
//        temp.orderBy(temp.col("count").desc()).show();
        wordDS.groupBy("value").count().orderBy(col("count").desc()).show();

    }
}
