package org.puchen.dome

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.protobuf.ProtobufUtil
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hive.metastore.hbase.HBaseConnection
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.util.Base64

object HabseDome {

  def main(args: Array[String]): Unit = {
    val hbaseConf: Configuration = HBaseConfiguration.create()
    val sc: SparkContext = new SparkContext(new SparkConf().setAppName("Spark").setMaster("local[*]"))
    val scan = new Scan();
    scan.addFamily(Bytes.toBytes("c"))//要读取的列簇
//    scan.setTimeStamp(timeStamp)//指定一个要读取的版本（如果只是拿列修饰符对应最新，可以不写）
//    scan.withStartRow(Bytes.toBytes(startRow))//要遍历的rowkey开始值（可以精确的也可以只是前缀），如果是全表可以不写
//    scan.withStopRow(Bytes.toBytes(stopRow))//要遍历的rowkey结束值（可以精确的也可以只是前缀），如果是全表可以不写

//    hbaseConf.set(TableInputFormat.SCAN, Base64.encodeBytes(ProtobufUtil.toScan(scan).toByteArray))
//    hbaseConf.set(TableInputFormat.INPUT_TABLE, Hbase的表名)
//
//    /**
//     * 初始化spark
//     */
//    val sparkName = ”read_Hbase“
//    val sparkConf = new SparkConf().setAppName(sparkName)
//      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//      .set("spark.kryoserializer.buffer.max.mb", "512")
//    val sc = new SparkContext(sparkConf)
//
//    /**获取hbase数据*/
//    val dataRDD: RDD[(ImmutableBytesWritable, Result)] =sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable],classOf[Result])
//

  }
}
