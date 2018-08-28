package com.pirate.esredisdemo.service.impl;

import com.pirate.esredisdemo.domain.EsAccount;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceImplTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void queryAll() {

        //获取一条
        GetQuery getQuery = new GetQuery();
        getQuery.setId("10");
        EsAccount esAccount = elasticsearchTemplate.queryForObject(getQuery, EsAccount.class);
        System.out.println(esAccount);

        //matchAll
        MatchAllQueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        PageRequest page = PageRequest.of(2, 10);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery)
                .withPageable(page)
//                .withPageable(PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id")))//分页+排序
                .build();
        List<EsAccount> esAccounts = elasticsearchTemplate.queryForList(searchQuery, EsAccount.class);
        AggregatedPage<EsAccount> esAccounts1 = elasticsearchTemplate.queryForPage(searchQuery, EsAccount.class);
        System.out.println(esAccounts.size());
    }

    @Test
    public void test() {
        queryById();
    }

    /**
     * 索引或者更新单条数据
     */
    public void insertOrUpdateById() {
        Student student = new Student(18, "zhangsan", "zhang", 29, 1, new ArrayList<>());
        IndexQuery indexQuery = new IndexQueryBuilder().
                withIndexName("student").
                withType("student").
                withId(student.getId().toString())
                .withObject(student).build();
        String index = elasticsearchTemplate.index(indexQuery);//返回的是设置的id值
    }

    /**
     * 批量索引或者更新数据
     */
    public void batchInsertOrUpdate() {
        List<IndexQuery> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student = new Student(i, "zhangsan " + i, i % 2 == 0 ? "zhang" : "li", 16 + i,
                    i % 3, new ArrayList<>());
            IndexQuery indexQuery = new IndexQueryBuilder().
                    withIndexName("student").
                    withType("student").
                    withId(student.getId().toString())
                    .withObject(student).build();


            list.add(indexQuery);
        }
        elasticsearchTemplate.bulkIndex(list);
    }


    public void queryById() {

        //select by id
        GetQuery getQuery = new GetQuery();
        getQuery.setId("4");
        Student student = elasticsearchTemplate.queryForObject(getQuery, Student.class);
        System.out.println(student);

        //采用bool中
        BoolQueryBuilder must = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("name", "zhangsan"))
                .filter(QueryBuilders.rangeQuery("age").gte(2));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                //.withQuery(QueryBuilders.queryStringQuery("zhang"))
                    //.withQuery(QueryBuilders.termsQuery("name","zhangsan")) // name=zhangsan
                //.withQuery(QueryBuilders.fuzzyQuery("name", "zhangsan"))// name like '%zhangsan%'
                //.withQuery(QueryBuilders.matchQuery("name","zhangsan"))
                //.withQuery(QueryBuilders.matchQuery("name","zhangsan lisi"))//匹配分词后的任何一个词
                //.withQuery(QueryBuilders.matchPhraseQuery("name","zhangsan lisi"))//精确匹配分词后的每一个词,位置敏感
                //.withQuery(QueryBuilders.matchPhraseQuery("name", "zhangsan lisi").slop(1))//slop与词条位置有关
                //.withQuery(QueryBuilders.matchQuery("name", "zhangsan lisi"))//对查询字符串分词，只要有一个匹配就可以，注意与term的区别
                .withQuery(must)
                //.withQuery(QueryBuilders.termsQuery("name", "zhangsan lisi"))//对查询字符串不分词，但是实际数据是否分词要根据实际的配置
                .withPageable(PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id")))//分页+排序
                // .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC)) // order by id desc
                .withHighlightFields(new HighlightBuilder.Field("name"))
                .build();
        List<Student> studentList = elasticsearchTemplate.queryForList(searchQuery, Student.class);
        //高亮
        Page<Student> students = elasticsearchTemplate.queryForPage(searchQuery, Student.class
                , new
                        SearchResultMapper() {
                            @Override
                            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable
                                    pageable) {
                                List<Student> list = new ArrayList<>();

                                long totalHits = searchResponse.getHits().getTotalHits();
                                for (SearchHit searchHit : searchResponse.getHits()) {
                                    Map<String, Object> source = searchHit.getSource();
                                    Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();

                                    Student s = new Student();
                                    s.setId(Integer.valueOf(source.get("id").toString()));
                                    HighlightField name = highlightFields.get("name");
                                    System.out.println();
                                    s.setName(name.getFragments()[0].toString());
                                    list.add(s);
                                }


                                AggregatedPage page = new AggregatedPageImpl(list);
                                return page;
                            }
                        }
        );
        long totalElements = students.getTotalElements();
        int number = students.getNumber();
        int totalPages = students.getTotalPages();
        studentList = students.getContent();

        System.out.println(studentList);


    }

}