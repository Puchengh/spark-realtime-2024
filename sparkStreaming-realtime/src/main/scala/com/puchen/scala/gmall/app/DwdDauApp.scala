package com.puchen.scala.gmall.app

import com.alibaba.fastjson.JSON
import com.puchen.scala.gmall.bean.{DauInfo, PageLog}
import com.puchen.scala.gmall.util.{MyBeanUtils, MyKafkaUtils, MyOffsetsUtils, MyRedisUtils}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.{lang, util}
import java.util.Date
import scala.collection.mutable.ListBuffer

/**
 * 日活宽表
 * 1.准备事实环境
 * 2.redis中读取偏移量
 * 3.从Kafaka中消费数据
 * 4.提取偏移量结束点
 * 5.处理数据
 *    5.1转换数据结构
 *    5.2去重
 *    5.3维度关联
 * 6.写入es中
 * 7.提交offset
 */
object DwdDauApp {

  def main(args: Array[String]): Unit = {
    //1.准备实时环境
    val conf: SparkConf = new SparkConf().setAppName("dwd_dau_app").setMaster("local[4]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))

    //2.redis中读取偏移量
    val topicName: String = "DWD_PAGE_LOG_TOPIC_1018"
    val groupId: String = "DWD_DAU_GROUP"
    val offsets: Map[TopicPartition, Long] = MyOffsetsUtils.reaOffset(topicName, groupId)
    // 3.从Kafaka中消费数据
    var kafkaDStream: InputDStream[ConsumerRecord[String, String]] = null
    if(offsets != null && offsets.nonEmpty){
      kafkaDStream = MyKafkaUtils.getKafkaDStram(ssc, topicName, groupId, offsets)
    }else{
      kafkaDStream = MyKafkaUtils.getKafkaDStram(ssc, topicName, groupId)
    }
    //4.提取偏移量结束点
    var offsetRanges: Array[OffsetRange] = null
    val offsetRangsDStream: DStream[ConsumerRecord[String, String]] = kafkaDStream.transform(
      (rdd: RDD[ConsumerRecord[String, String]]) => {
        offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    )
    //5.处理数据
    //    5.1转换数据结构
    val pageLogDStream: DStream[PageLog] = offsetRangsDStream.map(
      (record: ConsumerRecord[String, String]) => {
        val value: String = record.value()
        val pageLog: PageLog = JSON.parseObject(value, classOf[PageLog])
        pageLog
      }
    )
    //    pageLogDStream.cache()
    //    pageLogDStream.foreachRDD(
    //      rdd =>
    //        println("自我审查前=====>" + rdd.count())
    //    )
    //        pageLogDStream.print(100)
    //    5.2去重
    //自我审查: 将页面访问数据last_page_id不为空的数据过滤掉
    val filterDStream: DStream[PageLog] = pageLogDStream.filter(
      pageLog => pageLog.last_page_id == null
    )
    //    filterDStream.cache()
    //    filterDStream.foreachRDD(
    //        rdd =>
    //        println("自我审查后---->" + rdd.count())
    //    )

    //第三方审查 通过redis将当日活跃的mid维护起来 自我审查后的每天数据需要到redis中进行比对
    //redis如何维护日活状态
    //类型 set
    //key： DAU:日期date
    //value：mid的集合
    //写入API：lpush/rpush(list)   sadd(set)
    //读取API：lrange(list)         smembers(set)
    //过期：24h

    //    filterDStream.filter()  //每条数据执行一次 redis连接太频繁
    val redisFilterDStream: DStream[PageLog] = filterDStream.mapPartitions(
      (pageLogIter: Iterator[PageLog]) => {

        val pageLogList: List[PageLog] = pageLogIter.toList
    //        println("第三方审查前：" + pageLogList.size)

        val jedis: Jedis = MyRedisUtils.getJedisClient
        val sdf: SimpleDateFormat = new SimpleDateFormat("yyy-MM-dd")
        val pageLogs: ListBuffer[PageLog] = ListBuffer[PageLog]()
        //提取每条数据中的mid(日活的统计基于mid 也可以基于uid)
        for (pageLog <- pageLogList) {
          val mid: String = pageLog.mid
          //获取日期
          val ts: Long = pageLog.ts
          val date: Date = new Date(ts)
          val dateStr: String = sdf.format(date)
          val redisDauKey: String = s"DAU:${dateStr}"
          //下面代码在分布式环境中 存在并发问题 可能多个并行度同时进入到if中 导致最终保存多条同一个mid的数据
          //          //list操作
          //          val mids: util.List[String] = jedis.lrange(redisDauKey, 0, -1)
          //          if (!mids.contains(mid)) {
          //            jedis.lpush(redisDauKey,mid)
          //            pageLogs.append(pageLog)
          //          }
          //
          //          //set操作
          //          val setMids: util.Set[String] = jedis.smembers(redisDauKey)
          //          if (!setMids.contains(mid)) {
          //            jedis.sadd(redisDauKey,mid)
          //            pageLogs.append(pageLog)
          //          }
          val isFlag: lang.Long = jedis.sadd(redisDauKey, mid) //如果放进去则返回1  插入不进去返回的是0
          if (isFlag == 1L) {
            pageLogs.append(pageLog)
          }
        }
        jedis.close()
    //        println("第三方审查后：" + pageLogs.size)
        pageLogs.iterator
      }
    )
    //    redisFilterDStream.print()
    //    5.3维度关联
    redisFilterDStream.mapPartitions(
      pageLogIter => {
        for (pageLog <- pageLogIter) {
          //(1)将pageLog中以后用到的字段拷贝DauInfo中  对应赋值
                    val dauInfo: DauInfo = new DauInfo()
          //          dauInfo.mid = pageLog.mid
          //通过对象拷贝
          MyBeanUtils.copyProperties(pageLog,dauInfo)
          //(2)补充维度信息
          //    =>用户信息维度
          //    =>地区信息维度
          //    =>日期字段处理
        }

       null
      }
    )

    // 6.写入es中
    //7.提交offset
    
    ssc.start()
    ssc.awaitTermination()
  }
}
