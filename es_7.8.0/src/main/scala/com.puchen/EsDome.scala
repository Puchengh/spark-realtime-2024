import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import org.apache.http.HttpHost
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.get.{GetRequest, GetResponse}
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search
import org.elasticsearch.action.search.{SearchRequest, SearchResponse}
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.{RequestOptions, RestClient, RestClientBuilder, RestHighLevelClient}
import org.elasticsearch.common.text.Text
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.index.engine.Engine.DeleteResult
import org.elasticsearch.index.query.{BoolQueryBuilder, MatchQueryBuilder, QueryBuilders, RangeQueryBuilder, TermQueryBuilder}
import org.elasticsearch.index.reindex.UpdateByQueryRequest
import org.elasticsearch.script.{Script, ScriptType}
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.aggregations.{AggregationBuilders, Aggregations, BucketOrder}
import org.elasticsearch.search.aggregations.InternalOrder.Aggregation
import org.elasticsearch.search.aggregations.bucket.terms.{ParsedTerms, Terms, TermsAggregationBuilder}
import org.elasticsearch.search.aggregations.metrics.{AvgAggregationBuilder, ParsedAvg}
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.fetch.subphase.highlight.{HighlightBuilder, HighlightField}
import org.elasticsearch.search.sort.SortOrder

import java.util

object EsDome {


  def main(args: Array[String]): Unit = {

    //    println(client)
    //    put()
    //    post()
    //    bulk()
    //    update()
    //    updateByQuery()
    //    delete()
    //    getById()
//    searchByFilter()
    searchByAgg()
    close()
  }


  /**
   * 单条查询
   */

  def getById(): Unit = {
    val getRequest: GetRequest = new GetRequest("movie1018", "1001")
    val getResponse: GetResponse = client.get(getRequest, RequestOptions.DEFAULT)
    val sourceAsString: String = getResponse.getSourceAsString
    println(sourceAsString)
  }

  /**
   * 条件查询
   * 查询 doubanScore>=5.0 关键词搜索 red sea
   * 关键词高亮显示
   * 显示第一页，每页 2 条
   * 按 doubanScore 从大到小排序
   */

  def searchByFilter(): Unit = {

    val searchRequest: SearchRequest = new SearchRequest("movie_index")
    val searchSourceBuilder: SearchSourceBuilder = new SearchSourceBuilder()
    //query
    //bool
    val boolQueryBuilder: BoolQueryBuilder = QueryBuilders.boolQuery()
    //filter
    val rangeQueryBuilder: RangeQueryBuilder = QueryBuilders.rangeQuery("doubanScore").gte(5.0)
    boolQueryBuilder.filter(rangeQueryBuilder)
    //must
    val matchQueryBuilder: MatchQueryBuilder = QueryBuilders.matchQuery("name", "red sea")
    boolQueryBuilder.must(matchQueryBuilder)
    searchSourceBuilder.query(boolQueryBuilder)

    //高亮
    val highlightBuilder: HighlightBuilder = new HighlightBuilder()
    highlightBuilder.field("name")
    searchSourceBuilder.highlighter(highlightBuilder)
    //分页
    searchSourceBuilder.from(0)
    searchSourceBuilder.size(1)
    //排序
    searchSourceBuilder.sort("doubanScore", SortOrder.DESC)


    searchRequest.source(searchSourceBuilder)
    val searchResponse: SearchResponse = client.search(searchRequest, RequestOptions.DEFAULT)

    //获取总条数
    val toalDocs: Long = searchResponse.getHits.getTotalHits.value

    println(toalDocs)
    //明细
    val hits: Array[SearchHit] = searchResponse.getHits.getHits
    for (hit <- hits) {
      //数据
      val dataJson: String = hit.getSourceAsString
      //      val map: util.Map[String, AnyRef] = hit.getSourceAsMap
      val highlightFields: util.Map[String, HighlightField] = hit.getHighlightFields
      val highlightField: HighlightField = highlightFields.get("name")
      val fragments: Array[Text] = highlightField.getFragments
      val highLightValue: String = fragments(0).toString
      println("明细数据:" + dataJson)
      println("高亮:" + highLightValue)
    }

  }

  /**
   * 聚合查询
   * 查询每位演员参演的电影的平均分，倒叙排序
   */

  def searchByAgg(): Unit = {
    val searchRequest: SearchRequest = new SearchRequest("movie_index")
    val searchSourceBuilder: SearchSourceBuilder = new SearchSourceBuilder()
    //不要明细
    searchSourceBuilder.size(0)
    //group
    val termsAggregationBuilder: TermsAggregationBuilder = AggregationBuilders.terms("groupbyactorname").field("actorList.name.keyword").size(10)
      .order(BucketOrder.aggregation("doubanscoreavg",false))

    //avg
    val avgAggregationBuilder: AvgAggregationBuilder = AggregationBuilders.avg("doubanscoreavg").field("doubanScore")
    termsAggregationBuilder.subAggregation(avgAggregationBuilder)

    searchSourceBuilder.aggregation(termsAggregationBuilder)
    searchRequest.source(searchSourceBuilder)
    val searchResponse: SearchResponse = client.search(searchRequest, RequestOptions.DEFAULT)

    val aggregations: Aggregations = searchResponse.getAggregations
    val groupbyactorname: ParsedTerms = aggregations.get[ParsedTerms]("groupbyactorname")
    val buckets: util.List[_ <: Terms.Bucket] = groupbyactorname.getBuckets
    import scala.collection.JavaConverters._
    for (bucket <- buckets.asScala) {

      //电影个数
      val count: Long = bucket.getDocCount
      val actorName: String = bucket.getKeyAsString
      val aggregations1: Aggregations = bucket.getAggregations
      val doubanscoreavgParsedAvg: ParsedAvg = aggregations1.get[ParsedAvg]("doubanscoreavg")

      val avgScore: Double = doubanscoreavgParsedAvg.getValue
      println(s"$actorName 共参演了:$count 电影 平均分:$avgScore")
    }

  }

  /**
   * 删除
   */
  def delete(): Unit = {
    val result: DeleteRequest = new DeleteRequest("movie1018", "s1XR_48B8EATqJp4hPZY")
    client.delete(result, RequestOptions.DEFAULT)
  }

  /**
   * 单条修改
   */
  def update(): Unit = {
    val request: UpdateRequest = new UpdateRequest("movie1018", "1001")
    request.doc("movie_name", "功夫")
    client.update(request, RequestOptions.DEFAULT)
  }

  /**
   * 条件修改
   */
  def updateByQuery(): Unit = {
    val request: UpdateByQueryRequest = new UpdateByQueryRequest("movie1018")
    //query
    //    val termQueryBuilder: TermQueryBuilder = new TermQueryBuilder("movie_name.keyword","湄公河行动")
    val boolBuilder: BoolQueryBuilder = QueryBuilders.boolQuery()
    val termQueryBuilder: TermQueryBuilder = QueryBuilders.termQuery("movie_name.keyword", "速度与激情1")
    boolBuilder.filter(termQueryBuilder)
    request.setQuery(boolBuilder)
    //update
    val params: util.HashMap[String, AnyRef] = new util.HashMap[String, AnyRef]()
    params.put("newName", "湄公河行动")

    val script: Script = new Script(
      ScriptType.INLINE,
      Script.DEFAULT_SCRIPT_LANG,
      "ctx._source['movie_name']=params.newName",
      params
    )
    request.setScript(script)

    client.updateByQuery(request, RequestOptions.DEFAULT)
  }


  /**
   * 批量写
   */
  def bulk(): Unit = {
    val request: BulkRequest = new BulkRequest()
    val movies: List[Movie] = List[Movie](
      Movie("1002", "长津湖"),
      Movie("1003", "水门桥"),
      Movie("1004", "狙击手"),
      Movie("1005", "熊出没")
    )
    for (movie <- movies) {
      val indexRequeest: IndexRequest = new IndexRequest("movie1018")
      val movieJson: String = JSON.toJSONString(movie, new SerializeConfig(true))
      indexRequeest.source(movieJson, XContentType.JSON)
      indexRequeest.id(movie.id)
      request.add(indexRequeest)
    }

    client.bulk(request, RequestOptions.DEFAULT)

  }

  /**
   * 增加 幂等 --指定docID
   */
  def put(): Unit = {
    val indexReq: IndexRequest = new IndexRequest()
    indexReq.index("movie1018")
    //指定doc
    val movie: Movie = Movie("1001", "速度与激情1")
    val movieJson: String = JSON.toJSONString(movie, new SerializeConfig(true))
    indexReq.source(movieJson, XContentType.JSON)
    //指定docid 不知道
    indexReq.id("1001")
    client.index(indexReq, RequestOptions.DEFAULT)
  }

  /**
   * 增加 非幂等 不指定docid就是非幂等写
   */

  def post(): Unit = {
    val indexReq: IndexRequest = new IndexRequest()
    indexReq.index("movie1018")
    //指定doc
    val movie: Movie = Movie("1001", "速度与激情1")
    val movieJson: String = JSON.toJSONString(movie, new SerializeConfig(true))
    indexReq.source(movieJson, XContentType.JSON)
    client.index(indexReq, RequestOptions.DEFAULT)

  }


  /**
   * 客户端对象
   */
  var client: RestHighLevelClient = create()

  def create(): RestHighLevelClient = {

    val restClientBuilder: RestClientBuilder = RestClient.builder(new HttpHost("localhost", 9200))
    val client: RestHighLevelClient = new RestHighLevelClient(restClientBuilder)
    client
  }

  /**
   * 关闭客户端对象
   */
  def close(): Unit = {
    if (client != null) {
      client.close()
    }
  }


}

case class Movie(id: String, movie_name: String) {}
