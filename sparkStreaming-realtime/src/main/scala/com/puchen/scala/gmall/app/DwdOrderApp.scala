package com.puchen.scala.gmall.app

import com.alibaba.fastjson.serializer.SerializeConfig
import com.alibaba.fastjson.{JSON, JSONObject}
import com.puchen.scala.gmall.bean.{OrderDetail, OrderInfo, OrderWide}
import com.puchen.scala.gmall.util.{MyKafkaUtils, MyOffsetsUtils, MyRedisUtils}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

import java.time.{LocalDate, Period}
import java.util
import scala.collection.mutable.ListBuffer

/**
 * 订单宽表任务
 *
 * 1.实时环境
 * 2.从redis中读取offset * 2
 * 3.从kafak中消费数据 * 2
 * 4.提取offset * 2
 * 5.数据处理
 * 5.1维度关联
 * 5.2双流join
 * 6.写入es
 * 7.提交offset * 2
 */
object DwdOrderApp {

  def main(args: Array[String]): Unit = {
    val sparkconf: SparkConf = new SparkConf().setAppName("dwd_order_app").setMaster("local[4]")
    val ssc: StreamingContext = new StreamingContext(sparkconf, Seconds(5))

    val orderinfoTopicName: String = "DWD_ORDER_INFO_I_1018"
    val orderinfoGroup: String = "DWD_ORDER_INFO:GROUP"
    val orderInfoOffsets: Map[TopicPartition, Long] = MyOffsetsUtils.reaOffset(orderinfoTopicName, orderinfoGroup)

    val orderDetailTopicName: String = "DWD_ORDER_DETAIL_I_1018"
    val orderDetailGroup: String = "DWD_ORDER_DETAIL:GROUP"
    val orderDetailOffsets: Map[TopicPartition, Long] = MyOffsetsUtils.reaOffset(orderDetailTopicName, orderDetailGroup)

    //3 从kafak消费数据
    var orderInfoKakfaDStream: InputDStream[ConsumerRecord[String, String]] = null
    if (orderInfoOffsets != null && orderInfoOffsets.nonEmpty) {
      orderInfoKakfaDStream = MyKafkaUtils.getKafkaDStram(ssc, orderinfoTopicName, orderinfoGroup, orderInfoOffsets)
    } else {
      orderInfoKakfaDStream = MyKafkaUtils.getKafkaDStram(ssc, orderinfoTopicName, orderinfoGroup)
    }


    var orderDetailKakfaDStream: InputDStream[ConsumerRecord[String, String]] = null
    if (orderDetailOffsets != null && orderDetailOffsets.nonEmpty) {
      orderDetailKakfaDStream = MyKafkaUtils.getKafkaDStram(ssc, orderDetailTopicName, orderDetailGroup, orderDetailOffsets)
    } else {
      orderDetailKakfaDStream = MyKafkaUtils.getKafkaDStram(ssc, orderDetailTopicName, orderDetailGroup)
    }


    var orderInfoOffsetRanges: Array[OffsetRange] = null
    val orderInfoOffsetDStream: DStream[ConsumerRecord[String, String]] = orderInfoKakfaDStream.transform(
      rdd => {
        orderInfoOffsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    )

    var orderDeatilOffsetRanges: Array[OffsetRange] = null
    val orderDeatilOffsetDStream: DStream[ConsumerRecord[String, String]] = orderDetailKakfaDStream.transform(
      rdd => {
        orderDeatilOffsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    )

    //5.处理数据
    val orderInfoDStream: DStream[OrderInfo] = orderInfoOffsetDStream.map(
      record => {
        val value: String = record.value()
        val orderInfo: OrderInfo = JSON.parseObject(value, classOf[OrderInfo])
        orderInfo
      }
    )
    //    orderInfoDStream.print(100)


    val orderDetailDStream: DStream[OrderDetail] = orderDeatilOffsetDStream.map(
      record => {
        val value: String = record.value()
        val orderDetail: OrderDetail = JSON.parseObject(value, classOf[OrderDetail])
        orderDetail
      }
    )
    //    orderDetailDStream.print(100)

    //5.2维度关联
    val orderInfoDimDStream: DStream[OrderInfo] = orderInfoDStream.mapPartitions(

      orderinfoIter => {
        // val orderInfos: ListBuffer[OrderInfo] = ListBuffer[OrderInfo]()
        val orderinfos: List[OrderInfo] = orderinfoIter.toList
        val jeids: Jedis = MyRedisUtils.getJedisClient
        for (orderinfo <- orderinfos) {

          //关联用户维度
          val uid: Long = orderinfo.user_id
          val redisUserKey: String = s"DIM:USER_INFO:$uid"
          val userInfoJson: String = jeids.get(redisUserKey)
          val userInfoJsonObj: JSONObject = JSON.parseObject(userInfoJson)
          //提起性别
          val gender: String = userInfoJsonObj.getString("gender")

          //提取生日
          val birthday: String = userInfoJsonObj.getString("birthday") //1973-01-02
          val birthdatLd: LocalDate = LocalDate.parse(birthday)
          val noeLd: LocalDate = LocalDate.now()

          val period: Period = Period.between(birthdatLd, noeLd)
          val age: Int = period.getYears
          orderinfo.user_gender = gender
          orderinfo.user_age = age

          //关联地区维度

          val provinceID: Long = orderinfo.province_id
          val proKey: String = s"DIM:BASE_PROVINCE:$provinceID"
          val proJson: String = jeids.get(proKey)
          val proJsonObj: JSONObject = JSON.parseObject(proJson)

          val name: String = proJsonObj.getString("name")
          val area_code: String = proJsonObj.getString("area_code") //行政区位码
          val iso_code: String = proJsonObj.getString("iso_code") //国际编码
          val iso_3166_2: String = proJsonObj.getString("iso_3166_2") //ISO3166编码

          orderinfo.province_name = name
          orderinfo.province_3166_2 = iso_3166_2
          orderinfo.province_area_code = area_code
          orderinfo.province_iso_code = iso_code
          //处理日期字段

          val createTime: String = orderinfo.create_time
          val createDtHr: Array[String] = createTime.split(" ")
          orderinfo.create_date = createDtHr(0)
          orderinfo.create_hour = createDtHr(1).split(":")(0)
          //          orderInfos.iterator

        }
        jeids.close()
        orderinfos.iterator
      }
    )

    //    orderInfoDimDStream.print(100)

    //双流join
    val orderinfoKVDStream: DStream[(Long, OrderInfo)] = orderInfoDimDStream.map(orderinfo => (orderinfo.id, orderinfo))
    val orderdetailKVDStream: DStream[(Long, OrderDetail)] = orderDetailDStream.map(orderdetail => (orderdetail.order_id, orderdetail))

    val orderjoinDstream: DStream[(Long, (Option[OrderInfo], Option[OrderDetail]))] = orderinfoKVDStream.fullOuterJoin(orderdetailKVDStream)

    val orderWideDStream: DStream[OrderWide] = orderjoinDstream.mapPartitions(
      orderJoiniter => {
        val jedis: Jedis = MyRedisUtils.getJedisClient
        val orderwides: ListBuffer[OrderWide] = ListBuffer[OrderWide]()
        for ((key, (infoOP, detailOP)) <- orderJoiniter) {
          //orderinfo有orderdetail有
          if (infoOP.isDefined) {
            //取出orderinfo
            val orderinfo: OrderInfo = infoOP.get
            if (detailOP.isDefined) {
              val orderdetail: OrderDetail = detailOP.get
              //组装成功
              val orderWide: OrderWide = new OrderWide(orderinfo, orderdetail)
              orderwides.append(orderWide)
            }
            //orderinfo有orderdetail没有
            //不管orderdetail有没有，orderinfo都要写缓存和读缓存
            //orderinfo写缓存
            //类型 String
            //key ORDERJOIN:ORDER_INFO:ID
            //value json格式的数据
            //写API  set
            //读API  get
            //是否过期 24小时过期

            val redisOrdewrInfokey: String = s"ORDERJOIN:ORDER_INFO:${orderinfo.id}"
            //            jedis.set(redisOrdewrInfokey, JSON.toJSONString(orderinfo, new SerializeConfig(true)))
            //            jedis.expire(redisOrdewrInfokey,24*3600)
            jedis.setex(redisOrdewrInfokey, 24 * 3600, JSON.toJSONString(orderinfo, new SerializeConfig(true)))
            //orderinfo读缓存

            val redisOrderDeteailKey: String = s"ORDERJOIN:ORDER_DETAIL:${orderinfo.id}"
            val orderDetails: util.Set[String] = jedis.smembers(redisOrderDeteailKey)

            if (orderDetails != null && orderDetails.size() > 0) {
              import scala.collection.JavaConverters._
              for (orderDetailJson <- orderDetails.asScala) {
                val orderdetail: OrderDetail = JSON.parseObject(orderDetailJson, classOf[OrderDetail])
                //组装成功
                val orderWide: OrderWide = new OrderWide(orderinfo, orderdetail)
                orderwides.append(orderWide)
              }
            }

          } else {
            //orderinfo没有orderdetail有
            //读缓存
            val orederDetail: OrderDetail = detailOP.get
            val redisOrderinfoKey: String = s"ORDERJOIN:ORDER_DETAIL:${orederDetail.order_id}"
            val orderInfoJson: String = jedis.get(redisOrderinfoKey)
            if (orderInfoJson != null && orderInfoJson.size > 0) {
              val orderInfo: OrderInfo = JSON.parseObject(orderInfoJson, classOf[OrderInfo])
              val orderWide: OrderWide = new OrderWide(orderInfo, orederDetail)
              orderwides.append(orderWide)

            } else {
              //写缓存
              //类型 set
              //key ORDERJOIN:ORDER_DETAIL:OREDERID
              //value 多个json的集合
              //写API  sadd
              //读API  smembers
              //是否过期 24小时过期
              val redisOrderDeteailKey: String = s"ORDERJOIN:ORDER_DETAIL:${orederDetail.order_id}"
              jedis.sadd(redisOrderDeteailKey, JSON.toJSONString(redisOrderDeteailKey, new SerializeConfig(true)))
              jedis.expire(redisOrderDeteailKey, 24 * 3600)
            }
          }
        }
        jedis.close()
        orderwides.iterator
      }

    )

    orderWideDStream.print(100)


    ssc.start()
    ssc.awaitTermination()
  }
}
