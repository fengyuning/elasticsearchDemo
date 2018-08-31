package com.pirate.esredisdemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pirate.esredisdemo.dao.AccountDao;
import com.pirate.esredisdemo.dao.EsAccountDao;
import com.pirate.esredisdemo.domain.EsAccount;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestUtils;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.missing.MissingAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentileRanksAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentilesAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.scripted.ScriptedMetricAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.facet.FacetRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class esTest {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private EsAccountDao esAccountDao;

    /**
     *
     */
    @Test
    public void other() {
        EsAccount esAccount = new EsAccount();

        boolean b = esAccountDao.existsById(1);
        EsAccount index = esAccountDao.index(esAccount);    //update
        esAccountDao.refresh();
        System.out.println("真的难");
    }

    /**
     * 可以插入一个或者一群
     */
    @Test
    public void save() {
        EsAccount esAccount = new EsAccount();
        List<EsAccount> list = new ArrayList<>();
        esAccountDao.save(esAccount);
        Iterable<EsAccount> s = esAccountDao.saveAll(list);

        System.out.println("真的难");
    }

    /**
     * 可以插入一个或一群
     */
    @Test
    public void delete() {
        ArrayList list = new ArrayList();
        Iterable<EsAccount> all = null;

        esAccountDao.deleteById(1);
        esAccountDao.deleteAll();
        esAccountDao.delete(new EsAccount());
        esAccountDao.deleteAll(all);
    }

    /**
     * 关于text的排序还不是很清楚
     * 应该要在field上做文章
     */
    @Test
    public void find() {
        List<Integer> ids = new ArrayList() {{
            add(1);
            add(2);
        }};
        //可以通过id查询一条,或者ids查询一批
        Optional<EsAccount> account = esAccountDao.findById(1);
        Iterable<EsAccount> allById = esAccountDao.findAllById(ids);

        //findAll 可以排序(sort)，分页(Pageable)，分页+排序(Pageable)
        Iterable<EsAccount> all = esAccountDao.findAll(); //没条件

        //order(如果要对String对象做排序需要修改mapping)
        Sort.Order order1 = Sort.Order.asc("id");
        Sort.Order order2 = Sort.Order.desc("balance");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        //sort
        Sort sort1 = Sort.by("firstName.keyword", "balance", "accountNumber");
        Sort sort2 = Sort.by(Sort.Direction.ASC, "lastName", "age", "address");
        Sort sort3 = Sort.by(order1, order2);
        Sort sort4 = Sort.by(orders);
        Iterable<EsAccount> all1 = esAccountDao.findAll(sort1);
        Iterable<EsAccount> all2 = esAccountDao.findAll(sort2);
        Iterable<EsAccount> all3 = esAccountDao.findAll(sort3);
        Iterable<EsAccount> all4 = esAccountDao.findAll(sort4);

        //Pageable
        PageRequest pageRequest1 = PageRequest.of(0, 5);
        PageRequest pageRequest2 = PageRequest.of(0, 5, sort1);//比较复杂的排序就像上面的例子构造sort
        PageRequest pageRequest3 = PageRequest.of(0, 5, Sort.Direction.ASC, "id");//比较简单的排序可以直接传
        Page<EsAccount> page1 = esAccountDao.findAll(pageRequest1);
        Page<EsAccount> page2 = esAccountDao.findAll(pageRequest2);
        Page<EsAccount> page3 = esAccountDao.findAll(pageRequest3);

        System.out.println("真的难");
    }

    /**
     * 在search查询有 QueryBuilder searchQuery和相似查询
     */
    @Test
    public void search() {
        MatchAllQueryBuilder allQuery = QueryBuilders.matchAllQuery();
        BoolQueryBuilder must = QueryBuilders.boolQuery();
        PageRequest pageRequest = PageRequest.of(0, 10);
        HighlightBuilder.Field nameField = new HighlightBuilder.Field("name");
        String[] fields = {"aa", "bb"};
        AvgAggregationBuilder balance = AggregationBuilders.avg("balance");

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(allQuery)            //查询条件
                .addAggregation(balance)         //聚合
//                .withFacet()                     //分组
//                .withSort()                      //排序
                .withPageable(pageRequest)       //分页
                .withHighlightFields(nameField)  //高亮
                .build();

        Iterable<EsAccount> search1 = esAccountDao.search(allQuery);
        Page<EsAccount> search2 = esAccountDao.search(allQuery, pageRequest);
        Page<EsAccount> search3 = esAccountDao.search(searchQuery);
        Page<EsAccount> search4 = esAccountDao.searchSimilar(new EsAccount(), fields, pageRequest);

    }

    /**
     * 度量操作 还有很多操作其实也不是很理解，需要不断练习之后才能明白
     */
    @Test
    public void MetricsAggregations() {
        //min max avg sum count
        MinAggregationBuilder minBalance = AggregationBuilders.min("minBalance").field("balance");
        MaxAggregationBuilder maxBalance = AggregationBuilders.max("maxBalance").field("balance");
        AvgAggregationBuilder avgBalance = AggregationBuilders.avg("avgBalance").field("balance");
        SumAggregationBuilder balanceSum = AggregationBuilders.sum("balanceSum").field("balance");
        ValueCountAggregationBuilder count = AggregationBuilders.count("count").field("balance"); //指令是value_count
        // stats 综合多个指标的参数
        ExtendedStatsAggregationBuilder stats = AggregationBuilders.extendedStats("stats").field("balance");
        ExtendedStatsAggregationBuilder extendedStats =
                AggregationBuilders.extendedStats("extendStats").field("balance"); //extended_stats
        //percentiles 计算百分比 一个算大于该百分比的值 一个算该值所处的位置
        PercentilesAggregationBuilder percent =
                AggregationBuilders.percentiles("percentiles").field("balance");
        PercentileRanksAggregationBuilder percentilesRank =
                AggregationBuilders.percentileRanks("percentilesRank").field("balance").values(50, 60); //percentile_ranks
        //count(distinct age)
        CardinalityAggregationBuilder ageNum =
                AggregationBuilders.cardinality("ageNum").field("age.keyword"); //count(distinct age)

        //分组 不知道为什么结果不能转成json
        TermsAggregationBuilder groupByAge = AggregationBuilders.terms("groupByAge").field("age.keyword").size(3);
        TermsAggregationBuilder groupByAgeOrdeByNum = AggregationBuilders.terms("groupByAge").field("age.keyword")
                .order(Terms.Order.term(true));
        //配合分组使用
        TopHitsAggregationBuilder top = AggregationBuilders.topHits("top").size(1).explain(true).from(10);
        ScriptedMetricAggregationBuilder scriptedMetric = AggregationBuilders.scriptedMetric("doc['balance'].value == 40540");

//        AggregationBuilders.geoBounds() 地理位置相关，貌似和我没关系就没研究
        NativeSearchQuery searchQuery1 = new NativeSearchQueryBuilder()
//                .addAggregation(minBalance)
//                .addAggregation(maxBalance)
//                .addAggregation(avgBalance)
//                .addAggregation(balanceSum)
//                .addAggregation(count)
//                .addAggregation(stats)
//                .addAggregation(extendedStats)
//                .addAggregation(percent)
//                .addAggregation(percentilesRank)
//                .addAggregation(ageNum)
                .addAggregation(groupByAge)
                .addAggregation(groupByAgeOrdeByNum.subAggregation(ageNum))
                .withPageable(PageRequest.of(0, 1))
                .build();
        Page<EsAccount> search = esAccountDao.search(searchQuery1);
//        System.err.println(JSONObject.toJSON(search));

        System.err.println("end");
    }

    /**
     *  还有很多需要自己去掌握
     */
    @Test
    public void bucketAggregations(){

        //这个是顺带查询和统计的方式 select avg(balance) from account where age=22 or age=23;
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("age", "22 23");
        AvgAggregationBuilder avgBalance = AggregationBuilders.avg("avgBalance").field("balance");
        //filter的方式
        FilterAggregationBuilder ageFilter = AggregationBuilders.filter("ageFilter", QueryBuilders.termQuery("age","22"));
        //select count(*) from account where address == ''
        MissingAggregationBuilder missingAddress = AggregationBuilders.missing("address");
        NestedAggregationBuilder address = AggregationBuilders.nested("address","address"); //不知道做什么


        NativeSearchQuery query = new NativeSearchQueryBuilder()
//                .withQuery(matchQuery)
//                .addAggregation(avgBalance)
//                .addAggregation(ageFilter.subAggregation(avgBalance))
                .withPageable(PageRequest.of(0,2))
                .build();
        Page<EsAccount> search = esAccountDao.search(query);
        System.err.println(JSONObject.toJSONString(search));

        System.err.println("end");
    }
}