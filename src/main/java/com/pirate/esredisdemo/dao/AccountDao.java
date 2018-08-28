package com.pirate.esredisdemo.dao;

import com.pirate.esredisdemo.domain.AccountDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountDao {
    int insert(AccountDto accountDto);

    AccountDto selectById(@Param("id") int id);

    int deleteById(@Param("id") int id);

    int insertList(@Param("list") List<AccountDto> list);

    List<AccountDto> selectList(@Param("skip") int skip, @Param("size") int size);
}
