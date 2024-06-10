package com.puchen.scala.gmall.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import org.apache.http.HttpHost
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.{RequestOptions, RestClient, RestClientBuilder, RestHighLevelClient}
import org.elasticsearch.common.xcontent.XContentType

/**
 * es的工具类 用于对es的读写操作
 */
object MyEsUtils {

  /**
   * 客户端对象
   */
  val esClient: RestHighLevelClient = build()

  def build(): RestHighLevelClient = {
    val builder: RestClientBuilder = RestClient.builder(new HttpHost(MyPropsUtils(MyConfig.ES_HOST), MyPropsUtils(MyConfig.ES_PORT).toInt))
    val client: RestHighLevelClient = new RestHighLevelClient(builder)
    client
  }

  /**
   * 关闭对象
   */
  def close(): Unit = {
    if (esClient != null) esClient.close()
  }

  /**
   * 1.批量写
   * 2.幂等写
   *
   * @param indexName
   */
  def bucketSave(indexName: String, docs: List[(String, AnyRef)]): Unit = {

    val bulkRequest: BulkRequest = new BulkRequest(indexName)
    for ((docId, docObj) <- docs) {
      val indexRequest: IndexRequest = new IndexRequest()
      val dataJson: String = JSON.toJSONString(docObj, new SerializeConfig(true))
      indexRequest.source(dataJson, XContentType.JSON)
      indexRequest.id(docId)
      bulkRequest.add(indexRequest)
    }
    esClient.bulk(bulkRequest, RequestOptions.DEFAULT)
  }
}
