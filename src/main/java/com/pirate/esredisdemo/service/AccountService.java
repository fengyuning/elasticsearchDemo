package com.pirate.esredisdemo.service;

import com.pirate.esredisdemo.domain.AccountDto;
import com.pirate.esredisdemo.domain.Request;

public interface AccountService {
    Request insert(AccountDto accountDto);

    Request getAccountById(int id);

    Request delAccountById(int id);

    Request getEsAccountList();

    Request delEsAccountList();

    Request insertEsAccountList2Db();

    Request dbAccountList2Db();

    Request getMapGroupByAge();

    Request test();
}
