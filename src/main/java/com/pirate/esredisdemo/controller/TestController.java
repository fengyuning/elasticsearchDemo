package com.pirate.esredisdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.pirate.esredisdemo.dao.AccountDao;
import com.pirate.esredisdemo.dao.EsAccountDao;
import com.pirate.esredisdemo.domain.AccountDto;
import com.pirate.esredisdemo.domain.EsAccount;
import com.pirate.esredisdemo.domain.Request;
import com.pirate.esredisdemo.service.AccountService;
import com.pirate.esredisdemo.utils.LogUtil;
import com.pirate.esredisdemo.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
public class TestController {
    private static final Logger logger = LogUtil.COMMON_LOGGER;

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
    public Request getMapGroupByAge() {
        return accountService.getMapGroupByAge();
    }

    @GetMapping("testLogstash")
    public Request testLog() {
        logger.info("往logstash打日志");
        return RequestUtils.success();
    }

    @Autowired
    private JedisPool jedisPool;

    @GetMapping("testRedis")
    public Request testRedis() {
        Jedis jedis = jedisPool.getResource();
        String set = jedis.set("问题大", "真的吗");
        String qwqw = jedis.get("问题大");
        return RequestUtils.success(qwqw);
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
