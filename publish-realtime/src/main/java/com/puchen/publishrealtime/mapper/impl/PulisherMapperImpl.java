package com.puchen.publishrealtime.mapper.impl;

import com.puchen.publishrealtime.bean.NameValue;
import com.puchen.publishrealtime.mapper.PulisherMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.management.Descriptor;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class PulisherMapperImpl implements PulisherMapper {


    @Autowired
    RestHighLevelClient esClient;

    private String dauIndexNamePrefix = "gmall_dau_info_1018_";
    private String orderIndexNamePrefix = "gmall_order_wide_1018_";


    @Override
    public Map<String, Object> searchDau(String td) {
        Map<String, Object> dauResults = new HashMap<>();
        dauResults.put("dauTotal", searchDauTotal(td));

        dauResults.put("dauTd", searchDauHr(td));

        //计算昨日
        LocalDate tdld = LocalDate.parse(td);
        LocalDate ydld = tdld.minusDays(1);

//        dauResults.put("dauYd", searchDauHr(ydld.toString()));
        dauResults.put("dauYd", searchDauHr("2022-03-28"));

        return dauResults;
    }

    @Override
    public List<NameValue> searchStatsByItem(String itemName, String date, String field) {
        ArrayList<NameValue> results = new ArrayList<>();

        String indexName = orderIndexNamePrefix + date;

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        //query
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("sku_name", itemName).operator(Operator.AND);
        searchSourceBuilder.query(matchQueryBuilder);

        //group by
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("groupby" + field).field(field).size(100);
        searchSourceBuilder.aggregation(termsAggregationBuilder);

        //sum
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("totalamount").field("split_total_amount");
        termsAggregationBuilder.subAggregation(sumAggregationBuilder);

        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedTerms parsedTerms = aggregations.get("groupby" + field);
            List<? extends Terms.Bucket> buckets = parsedTerms.getBuckets();
            for (Terms.Bucket bucket : buckets) {
                String key = bucket.getKeyAsString();
                Aggregations bucketAggregations = bucket.getAggregations();
                ParsedSum parsedSum = bucketAggregations.get("totalamount");
                double totalamount = parsedSum.getValue();
                results.add(new NameValue(key,totalamount));
            }
            return results;
        } catch (ElasticsearchStatusException ese) {
            if (ese.status() == RestStatus.NOT_FOUND) {
                log.warn("{}不存在！！！", indexName);
            }
        } catch (IOException e) {
            throw new RuntimeException("查询ES失败");
        }
        return results;
    }

    @Override
    public Map<String, Object> searchDetailByItem(String date, String itemName, Integer from, Integer pageSize) {

        HashMap<String, Object> results = new HashMap<>();
        String indexName = orderIndexNamePrefix + date;
        SearchRequest searchRequest = new SearchRequest();
        //明细字段
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"create_time","order_price","province_name","sku_name","sku_num","total_amount","user_age","user_gender"},null);

        //query
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("sku_name", itemName).operator(Operator.AND);
        searchSourceBuilder.query(matchQueryBuilder);

        //from
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(pageSize);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("sku_name");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
            long total = searchResponse.getHits().getTotalHits().value;
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            ArrayList<Map<String, Object>> sourceMaps = new ArrayList<>();
            for (SearchHit searchHit : searchHits) {
                //提取source
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                //提取高亮
                Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                HighlightField highlightField = highlightFields.get("sku_name");
                Text[] fragments = highlightField.getFragments();
                String hightLiterSkuName = fragments[0].toString();
                sourceAsMap.put("sku_name",hightLiterSkuName);
                sourceMaps.add(sourceAsMap);
            }
            //处理最终结果
            results.put("total",total);
            results.put("detail",sourceMaps);

            return results;
        } catch (ElasticsearchStatusException ese) {
            if (ese.status() == RestStatus.NOT_FOUND) {
                log.warn("{}不存在！！！", indexName);
            }
        } catch (IOException e) {
            throw new RuntimeException("查询ES失败");
        }

        return results;
    }

    public HashMap<String, Long> searchDauHr(String td){

        HashMap<String, Long> stringLongHashMap = new HashMap<>();

        String indexName = dauIndexNamePrefix + td;
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("groupby").field("hr").size(24);
        searchSourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
            ParsedTerms parsedTerms = response.getAggregations().get("groupby");
            List<? extends Terms.Bucket> buckets = parsedTerms.getBuckets();
            for (Terms.Bucket bucket : buckets) {
                String hr = bucket.getKeyAsString();
                long docCount = bucket.getDocCount();
                stringLongHashMap.put(hr,docCount);
            }
            return stringLongHashMap;
        } catch (ElasticsearchStatusException ese) {
            if (ese.status() == RestStatus.NOT_FOUND) {
                log.warn("{}不存在！！！", indexName);
            }
        } catch (IOException e) {
            throw new RuntimeException("查询ES失败");
        }
        return stringLongHashMap;
    }

    public Long searchDauTotal(String td) {
        String indexName = dauIndexNamePrefix + td;
        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);


        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
            return response.getHits().getTotalHits().value;
        } catch (ElasticsearchStatusException ese) {
            if (ese.status() == RestStatus.NOT_FOUND) {
                log.warn("{}不存在！！！", indexName);
            }
        } catch (IOException e) {
            throw new RuntimeException("查询ES失败");
        }
        return 0L;
    }
}
