import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import static org.apache.spark.sql.functions.col;

public class StructStreamingDemo {
    public static void main(String[] args) throws TimeoutException, StreamingQueryException {

        SparkSession spark = SparkSession.builder().appName("123").master("local[*]").getOrCreate();
        spark.sparkContext().setLogLevel("WARN");
        Dataset<Row> lines = spark.readStream()
                .format("socket")
                .option("host", "study")
                .option("port", "9999")
                .load();

        Dataset<String> ds = lines.as(Encoders.STRING());
        Dataset<String> wordDS = ds.flatMap((String line) -> Arrays.asList(line.split(" ")).iterator(), Encoders.STRING());

        //SQL
        wordDS.createOrReplaceTempView("t_word");
        Dataset<Row> result1 = spark.sql("select value,count(*) as counts from t_word group by value order by counts desc limit 10");

        //DSL

        Dataset<Row> result2 = wordDS.groupBy("value").count().orderBy(col("count").desc());

        result1.writeStream()
                .format("console")
                .outputMode(OutputMode.Complete())
                .start();
        result2.writeStream()
                .format("console")
                .outputMode(OutputMode.Complete())
                .start()
                .awaitTermination();

    }
}
