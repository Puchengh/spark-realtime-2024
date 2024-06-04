package com.puchen.scala.gmall.app

import java.util
import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import com.puchen.scala.gmall.util.{MyKafkaUtils, MyOffsetsUtils, MyRedisUtils}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.kafka.common.TopicPartition
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

/**
 * 业务数据消费分流
 * 1.准备实时环境
 * 2.从redis中去读偏移量
 * 3.从kafka中消费数据
 * 4.提取偏移量的结束点
 * 5.数据处理
 * （1）转换数据结构
 * （2）分流
 * 事实数据 -> kafka
 * 维度数据 -> redis
 * 6.flush kafka的缓冲区
 * 7.提取iffset
 */
object OdsBaseDbApp {
  def main(args: Array[String]): Unit = {
    //1.准备实时环境
    val conf: SparkConf = new SparkConf().setAppName("ods_base_db_app").setMaster("local[4]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))

    //2.redis中去读数据
    val topicName: String = "ODS_BASE_DB_1018"
    val groupId: String = "ODS_BASE_DB_GROUP_1018"
    val offset: Map[TopicPartition, Long] = MyOffsetsUtils.reaOffset(topicName, groupId)

    //3.从kafka中消费数据
    var kafkaDstream: InputDStream[ConsumerRecord[String, String]] = null
    if (offset != null && offset.nonEmpty) {
      kafkaDstream = MyKafkaUtils.getKafkaDStram(ssc, topicName, groupId, offset)
    } else {
      kafkaDstream = MyKafkaUtils.getKafkaDStram(ssc, topicName, groupId)
    }

    //4.提取偏移量结束点
    var offsetRanges: Array[OffsetRange] = null
    val offsetRangesDStream: DStream[ConsumerRecord[String, String]] = kafkaDstream.transform(
      rdd => {
        offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    )
    //5.处理数据
    //5.1转换数据结构
    val jsonObjDStream: DStream[JSONObject] = offsetRangesDStream.map(
      consumerRecord => {
        val dataJson: String = consumerRecord.value()
        val JSONObject: JSONObject = JSON.parseObject(dataJson)
        JSONObject
      }
    )
    //        jsonObjDStream.print(100)
    //5.2分流

//    //事实表清单
//    val factTables: Array[String] = Array[String]("order_info", "order_detail")
//    //维度表清单
//    val dimTables: Array[String] = Array[String]("user_info", "base_province")
    jsonObjDStream.foreachRDD(
      rdd => {

        //如何的动态配置表清单? 将变禽蛋维护到redis中 实时任务重动态的到rredis中获取
        //类型： set比较合适
        //key：FACT:TABLES  DIM:TABLES
        //value：tableName
        //写入API： sadd
        //读取API： smembers
        //过期时间： 一定不过期
        val redisFactKeys: String = "FACT:TABLES"
        val redisDimKeys: String = "DIM:TABLES"
        val jedisTable: Jedis = MyRedisUtils.getJedisClient
        //事实表清单
        val factTables: util.Set[String] = jedisTable.smembers(redisFactKeys)
        println("factTables"+factTables)
        //做成广播变量
        val factTablesBC: Broadcast[util.Set[String]] = ssc.sparkContext.broadcast(factTables)
        //维度表清单
        val dimTables: util.Set[String] = jedisTable.smembers(redisDimKeys)
        println("dimTables"+dimTables)
        val dimTablesBC: Broadcast[util.Set[String]] = ssc.sparkContext.broadcast(factTables)
        jedisTable.close()

        rdd.foreachPartition(
          jsonObjIter => {
            //开启redis连接  只能在excutor端开启，driver端执行的话会涉及到数据传输，必须要序列化，而redis不支持序列化，不能传输
            val jedis: Jedis = MyRedisUtils.getJedisClient
            for (jsonObj <- jsonObjIter) {
              //提取类型
              val operType: String = jsonObj.getString("type")
              val operValue: String = operType match {
                case "bootstrap-insert" => "I"
                case "insert" => "I"
                case "update" => "U"
                case "delete" => "D"
                case _ => null
              }
              //判断操作类型 1.明确什么操作 2.过滤不需要的数据
              if (operValue != null) {
                val tableName: String = jsonObj.getString("table")

                if (factTablesBC.value.contains(tableName)) {
                  //提取数据
                  val data: String = jsonObj.getString("data")
                  //事实数据
                  val dwdTopicName: String = s"DWD_${tableName.toUpperCase}_${operValue}_1018"
                  MyKafkaUtils.send(dwdTopicName, data)
                }
                //广播变量中拿数据
                if (dimTablesBC.value.contains(tableName)) {
                  //维度数据
                  //类型 String  list set zset hash：
                  //       (1)用String比较合适,一条数据存成一个jsonString 性价比比较高
                  //       (2)整个表hash的 要考虑目前数据量的大小以及和数据量增长问题和高频访问问题
                  //       (3) 一条数据存成一个hash  比较方便的是任何查找数据,是否会高频访问
                  //key：DIM + tableName + 表主键id
                  //value：整条数据的JSON格式
                  //写入API：set
                  //读取API：get
                  //过期：不过期 是一个维度数据，肯定是不过期的
                  val dataObj: JSONObject = jsonObj.getJSONObject("data")
                  val id: String = dataObj.getString("id")
                  val redisKey: String = s"DIM:${tableName.toUpperCase}:$id"
//                  val jedis: Jedis = MyRedisUtils.getJedisClient
                  jedis.set(redisKey, dataObj.toJSONString)
//                  jedis.close()

                }
              }
            }
            //关闭连接
            jedis.close()
            //刷写缓冲区
            MyKafkaUtils.flush()
          }
        )
        //        提取offsets
        MyOffsetsUtils.saveOffset(topicName, groupId, offsetRanges)
      }
    )
    ssc.start()
    ssc.awaitTermination()
  }

}
