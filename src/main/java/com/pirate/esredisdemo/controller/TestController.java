package com.pirate.esredisdemo.controller;

import com.pirate.esredisdemo.domain.AccountDto;
import com.pirate.esredisdemo.domain.Request;
import com.pirate.esredisdemo.service.AccountService;
import com.pirate.esredisdemo.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

}
