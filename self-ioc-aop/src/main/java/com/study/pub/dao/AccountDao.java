package com.study.pub.dao;

import com.study.pub.pojo.Account;

public interface AccountDao {

   Account queryAccountByCardNo(String cardNo) throws Exception;

   int updateAccountByCardNo(Account account) throws Exception;
}
