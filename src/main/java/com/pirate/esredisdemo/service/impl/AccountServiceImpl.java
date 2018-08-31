package com.pirate.esredisdemo.service.impl;

import com.pirate.esredisdemo.dao.AccountDao;
import com.pirate.esredisdemo.dao.EsAccountDao;
import com.pirate.esredisdemo.domain.AccountDto;
import com.pirate.esredisdemo.domain.EsAccount;
import com.pirate.esredisdemo.domain.Request;
import com.pirate.esredisdemo.enums.EsEnum;
import com.pirate.esredisdemo.service.AccountService;
import com.pirate.esredisdemo.utils.RequestUtils;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private EsAccountDao esAccountDao;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Request insert(AccountDto accountDto) {
        accountDao.insert(accountDto);
        return RequestUtils.success();
    }

    @Override
    public Request getAccountById(int id) {
        return RequestUtils.success(accountDao.selectById(id));
    }

    @Override
    public Request delAccountById(int id) {
        accountDao.deleteById(id);
        return RequestUtils.success();
    }

    @Override
    public Request getEsAccountList() {
        Iterable<EsAccount> all = esAccountDao.findAll();
        long count = esAccountDao.count();
//        MatchAllQueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
//        NativeSearchQuery query = new NativeSearchQueryBuilder()
//                .withQuery(matchAllQuery)
//                .withPageable(PageRequest.of(5, 100, Sort.by("account_number", "balance")))//分页+排序
//                .build();
//        Page<EsAccount> esAccounts = elasticsearchTemplate.queryForPage(query, EsAccount.class);
        return RequestUtils.success(all);
    }

    @Override
    public Request insertEsAccountList2Db() {
        MatchAllQueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        for (int i = 0; ; i++) {
            NativeSearchQuery query = new NativeSearchQueryBuilder()
                    .withQuery(matchAllQuery)
                    .withPageable(PageRequest.of(i, EsEnum.MAX_SIZE.getSize()))
                    .build();
            List<EsAccount> esAccounts = elasticsearchTemplate.queryForList(query, EsAccount.class);
            if (esAccounts == null || esAccounts.size() == 0) {
                return RequestUtils.success();
            }

            List<AccountDto> collect = esAccounts.stream()
                    .filter(e -> e.getFirstName() != null)
                    .map(e -> {
                        AccountDto account = new AccountDto();
                        account.setAccountNumber(e.getAccountNumber());
                        account.setBalance(e.getBalance());
                        account.setFirstName(e.getFirstName());
                        account.setLastName(e.getLastName());
                        account.setAge(e.getAge());
                        account.setGender(e.getGender());
                        account.setAddress(e.getAddress());
                        account.setEmployer(e.getEmployer());
                        account.setEmail(e.getEmail());
                        account.setCity(e.getCity());
                        account.setState(e.getState());
                        return account;
                    }).collect(Collectors.toList());
            accountDao.insertList(collect);
        }
    }

    @Override
    public Request delEsAccountList() {
        esAccountDao.deleteAll();
        return RequestUtils.success();
    }

    @Override
    public Request dbAccountList2Db() {

        for (int i = 0; ; i++) {
            List<AccountDto> list = accountDao.selectList(i * EsEnum.MAX_SIZE.getSize(), EsEnum.MAX_SIZE.getSize());
            if (list == null || list.size() == 0) {
                return RequestUtils.success();
            }
            List<EsAccount> collect = list.stream().map(e -> {
                EsAccount esAccount = new EsAccount();
                esAccount.setAccountNumber(e.getAccountNumber());
                esAccount.setBalance(e.getBalance());
                esAccount.setFirstName(e.getFirstName());
                esAccount.setLastName(e.getLastName());
                esAccount.setAge(e.getAge());
                esAccount.setGender(e.getGender());
                esAccount.setAddress(e.getAddress());
                esAccount.setEmployer(e.getEmployer());
                esAccount.setEmail(e.getEmail());
                esAccount.setCity(e.getCity());
                esAccount.setState(e.getState());
                return esAccount;
            }).collect(Collectors.toList());
            esAccountDao.saveAll(collect);
        }
    }

    @Override
    public Request getMapGroupByAge() {
        List<AccountDto> accountDtos = accountDao.selectList(0, 0);
        Map<Integer, List<AccountDto>> age = accountDtos.stream().collect(Collectors.groupingBy(AccountDto::getAge));
        return RequestUtils.success(age);
    }

    @Override
    public Request test() {
        return RequestUtils.success(1/0);
    }
}
