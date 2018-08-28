package com.pirate.esredisdemo.service.impl;

import com.pirate.esredisdemo.dao.AccountDao;
import com.pirate.esredisdemo.dao.EsAccountDao;
import com.pirate.esredisdemo.domain.EsAccount;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
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
        EsAccount index = esAccountDao.index(esAccount);
        esAccountDao.refresh();
        long count = esAccountDao.count();
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
        list.remove()
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
                .withSort()                      //排序
                .withPageable(pageRequest)       //分页
                .withHighlightFields(nameField)  //高亮
                .build();

        Iterable<EsAccount> search1 = esAccountDao.search(allQuery);
        Page<EsAccount> search2 = esAccountDao.search(allQuery, pageRequest);
        Page<EsAccount> search3 = esAccountDao.search(searchQuery);
        Page<EsAccount> search4 = esAccountDao.searchSimilar(new EsAccount(), fields, pageRequest);

    }

}