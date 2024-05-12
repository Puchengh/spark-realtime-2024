package com.puchen.scala.gmall.app

import com.alibaba.fastjson.serializer.SerializeConfig
import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import com.puchen.scala.gmall.bean.{PageActionLog, PageDisPageLog, PageLog, StartLog}
import com.puchen.scala.gmall.util.MyPropsUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

//日志数据的消费分流

/**
 * 1.准备实时处理环境 StreamingContext
 *
 * 2.从kafka中消费数据
 *
 * 3.处理数据
 * 3.1转换数据结构
 * 3.2分流
 * 4.写出到DWD层
 *
 */
object OdsBaseLogApp {
  def main(args: Array[String]): Unit = {
    //准备实时环境
    //注意并行度kafka和topic的分区个数的对应关系
    val sparkConf: SparkConf = new SparkConf().setAppName("ods_base_log_app").setMaster("local[4]")
    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(5)) //注意Seconds
    //从kafka中消费数据

    //对应生成器中配置的主题名
    val topicName: String = "ODS_BASE_LOG_1018"
    val groupId: String = "ODS_BASE_LOG_GROUP_1018"
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = MyPropsUtils.getKafkaDStram(ssc, topicName, groupId)

    //    kafkaDStream.print(100)
    /**
     * 3.处理数据
     * 3.1 转换数据结构 专用数据结构 Bean 通用结构 Map JsonObject
     */
    val jsonjDStream: DStream[JSONObject] = kafkaDStream.map(
      consumerRecord => {
        //获取record中的value
        val log: String = consumerRecord.value()
        val jsonObject: JSONObject = JSON.parseObject(log)
        jsonObject
      }
    )
    //测试完成之后就相当于处理完了
    //    jsonjDStream.print(100)
    /**
     * 3.2分流
     * 日志数据
     * 公共字段
     * 页面数据
     * 曝光数据
     * 事件数据
     * 错误数据
     * 启动数据
     * 公共字段
     * 启动数据
     */

    val DWD_PAGE_LOG_TOPIC: String = "DWD_PAGE_LOG_TOPIC_1018" //页面访问
    val DWD_PAGE_DISPLAY_TOPIC: String = "DWD_PAGE_DISPLAY_TOPIC_1018" //页面曝光
    val DWD_PAGE_ACTIION_TOPIC: String = "DWD_PAGE_ACTIION_TOPIC_1018" //页面时间
    val DWD_START_LOG_TOPIC: String = "DWD_START_LOG_TOPIC_1018" //启动数据
    val DWD_ERROR_LOG_TOPIC: String = "DWD_ERROR_LOG_TOPIC_1018" //错误数据


    /**
     * 分流规则
     * 错误数据：不做任何的拆分，只要包含错误字段，直接整条数据发送到对应的topic
     * 页面数据：炒粉成页面访问 曝光 时间 分别发送对应不能的topic
     * 启动数据：发送到对应的topic
     */

    jsonjDStream.foreachRDD(
      rdd => rdd.foreach(
        jsonObj => {
          //分流过程
          //分流错误数据
          val errorObject: JSONObject = jsonObj.getJSONObject("err")
          if (errorObject != null) {

            MyPropsUtils.send(DWD_ERROR_LOG_TOPIC, jsonObj.toJSONString)
          } else {
            //提取公共字段
            val commonObject: JSONObject = jsonObj.getJSONObject("common")
            val ar: String = commonObject.getString("ar")
            val uid: String = commonObject.getString("uid")
            val os: String = commonObject.getString("os")
            val ch: String = commonObject.getString("ch")
            val is_new: String = commonObject.getString("is_new")
            val md: String = commonObject.getString("md")
            val mid: String = commonObject.getString("mid")
            val vc: String = commonObject.getString("vc")
            val ba: String = commonObject.getString("ba")
            //提取时间戳
            val ts: Long = jsonObj.getLong("ts")
            val pageObj: JSONObject = jsonObj.getJSONObject("page")
            if (pageObj != null) {
              //提取page字段
              val page_id: String = pageObj.getString("page_id")
              val item: String = pageObj.getString("item")
              val during_time: Long = pageObj.getLong("during_time")
              val item_type: String = pageObj.getString("item_type")
              val last_page_id: String = pageObj.getString("last_page_id")
              val source_type: String = pageObj.getString("source_type")
              //封装PageLog
              val pageLog: PageLog = PageLog(mid, uid, ar, ch, is_new, md, os, vc, ba, page_id, last_page_id, item, item_type, during_time, source_type, ts)
              //发送到page主题中--DWD_PAGE_LOG_TOPIC
              MyPropsUtils.send(DWD_PAGE_LOG_TOPIC, JSON.toJSONString(pageLog, new SerializeConfig(true))) //fastjson中有java是get set方法  scala没有、
              //提取曝光数据
              val displaysArray: JSONArray = jsonObj.getJSONArray("displays")
              if (displaysArray != null && displaysArray.size() > 0)

                for (i <- 0 until (displaysArray.size())) {
                  val displaysObj: JSONObject = displaysArray.getJSONObject(i)
                  val display_type: String = displaysObj.getString("display_type")
                  val displaysitem: String = displaysObj.getString("item")
                  val displaysitem_type: String = displaysObj.getString("item_type")
                  val pos_id: String = displaysObj.getString("pos_id")
                  val order: String = displaysObj.getString("order")
                  val pageDisPageLog: PageDisPageLog = PageDisPageLog(mid, uid, ar, ch, is_new, md, os, vc, ba, page_id, last_page_id, item, item_type, during_time, source_type, ts, display_type, displaysitem, displaysitem_type, pos_id, order)
                  //写到kafaka--DWD_PAGE_DISPLAY_TOPIC
                  MyPropsUtils.send(DWD_PAGE_DISPLAY_TOPIC, JSON.toJSONString(pageDisPageLog, new SerializeConfig(true)))
                }
              //提取事件数据
              val actionsArray: JSONArray = jsonObj.getJSONArray("actions")
              if (actionsArray != null && actionsArray.size() > 0) {
                for (i <- 0 until (actionsArray.size())) {
                  val actionsObj: JSONObject = actionsArray.getJSONObject(i)
                  val action_item: String = actionsObj.getString("item")
                  val action_id: String = actionsObj.getString("action_id")
                  val action_item_type: String = actionsObj.getString("item_type")
                  val action_ts: Long = actionsObj.getLong("ts")

                  val pageActionLog: PageActionLog = PageActionLog(mid, uid, ar, ch, is_new, md, os, vc, ba, page_id, last_page_id, item, item_type, during_time, source_type, action_id, action_item, action_item_type, action_ts, ts)

                  MyPropsUtils.send(DWD_PAGE_ACTIION_TOPIC, JSON.toJSONString(pageActionLog, new SerializeConfig(true)))
                }
              }
            }
            //启动数据
            val startObj: JSONObject = jsonObj.getJSONObject("start")
            if (startObj != null) {
              val start_entry: String = startObj.getString("entry")
              val start_open_ad_skip_ms: Long = startObj.getLong("open_ad_skip_ms")
              val start_open_ad_ms: Long = startObj.getLong("open_ad_ms")
              val start_loading_time: Long = startObj.getLong("loading_time")
              val start_open_ad_id: String = startObj.getString("open_ad_id")
              val startLog: StartLog = StartLog(mid, uid, ar, ch, is_new, md, os, vc, ba, start_entry, start_open_ad_id, start_loading_time, start_open_ad_ms, start_open_ad_skip_ms, ts)
              MyPropsUtils.send(DWD_START_LOG_TOPIC, JSON.toJSONString(startLog, new SerializeConfig(true)))
            }
          }

        }
      )

    )
    ssc.start()
    ssc.awaitTermination()
  }


}
