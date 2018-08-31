package com.pirate.esredisdemo.controller;

import com.pirate.esredisdemo.dao.AccountDao;
import com.pirate.esredisdemo.dao.EsAccountDao;
import com.pirate.esredisdemo.domain.AccountDto;
import com.pirate.esredisdemo.domain.EsAccount;
import com.pirate.esredisdemo.domain.Request;
import com.pirate.esredisdemo.service.AccountService;
import com.pirate.esredisdemo.utils.RequestUtils;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;

@RestController
public class TestController {

    @Autowired
    private AccountService accountService;

    @PostMapping("account")
    public Request insertAccount(AccountDto accountDto) {
        return accountService.insert(accountDto);
    }

    @GetMapping("account/{id}")
    public Request getAccount(@PathVariable("id") int id) {
        return accountService.getAccountById(id);
    }

    @DeleteMapping("account/{id}")
    public Request deleteAccount(@PathVariable("id") int id) {
        return accountService.delAccountById(id);
    }

    @PutMapping("account/{id}")
    public Request updateAccount(@PathVariable("id") int id, AccountDto accountDto) {
        return null;
    }

    @GetMapping("esAccountList")
    public Request getEsAccountList() {
        return accountService.getEsAccountList();
    }

    @DeleteMapping("esAccountList")
    public Request deleteEsAccountList() {
        return accountService.delEsAccountList();
    }

    @GetMapping("esAccountList2Db")
    public Request esAccountList2Db() {
        return accountService.insertEsAccountList2Db();
    }

    @GetMapping("dbAccountList2Es")
    public Request dbAccountList2Es() {
        return accountService.dbAccountList2Db();
    }

    @GetMapping("getMapGroupByAge")
    public Request getMapGroupByAge(){
        return accountService.getMapGroupByAge();
    }


    //------------------------------------------------------------
    @Autowired
    private EsAccountDao esAccountDao;
    @GetMapping("test")
    public Request test() {
        //分组
//        TermsAggregationBuilder groupByAge = AggregationBuilders.terms("groupByAge").field("age.keyword").size(3);
//        TermsAggregationBuilder groupByAge = AggregationBuilders.terms("groupByAge").field("age.keyword")
//                .order(Terms.Order.term(true));
//
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .addAggregation(groupByAge)
//                .build();
//        Page<EsAccount> search = esAccountDao.search(searchQuery);
//        Iterator<EsAccount> iterator = search.iterator();
//        Spliterator<EsAccount> spliterator = search.spliterator();
//
//        return RequestUtils.success(search);
        return accountService.test();
    }


}
