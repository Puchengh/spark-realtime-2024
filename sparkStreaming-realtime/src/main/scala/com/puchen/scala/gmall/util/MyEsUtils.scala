package com.puchen.scala.gmall.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import org.apache.http.HttpHost
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.{SearchRequest, SearchResponse}
import org.elasticsearch.client.indices.GetIndexRequest
import org.elasticsearch.client.{RequestOptions, RestClient, RestClientBuilder, RestHighLevelClient}
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.builder.SearchSourceBuilder

import java.util
import scala.collection.mutable.ListBuffer

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

  /**
   * GET gmall_dau_info_1018_2022-03-28/_search
   * {
   * "_source": "mid"
   * }
   *
   * @param indexName
   * @param fieldName
   * @return
   */
  def searchField(indexName: String, fieldName: String): List[String] = {
    //先判断索引是否存在
    val getIndexRequest: GetIndexRequest = new GetIndexRequest(indexName)
    val isExists: Boolean = esClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT)
    if(!isExists){
      return null
    }
    //正常查询es中所有的指定的字段
    val mids: ListBuffer[String] = ListBuffer[String]()
    val searchRequest: SearchRequest = new SearchRequest(indexName)
    val searchSourceBuilder: SearchSourceBuilder = new SearchSourceBuilder()
    searchSourceBuilder.fetchSource(fieldName, null).size(100000)
    searchRequest.source(searchSourceBuilder)
    //结果
    val searchResponse: SearchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT)
    val hits: Array[SearchHit] = searchResponse.getHits.getHits
    for (hit <- hits) {
      val sourceMap: util.Map[String, AnyRef] = hit.getSourceAsMap
      val mid: String = sourceMap.get(fieldName).toString
      mids.append(mid)
    }
    mids.toList
  }

  def main(args: Array[String]): Unit = {
    println(searchField("gmall_dau_info_1018_2022-03-28", "mid"))
    esClient.close()
  }
}
