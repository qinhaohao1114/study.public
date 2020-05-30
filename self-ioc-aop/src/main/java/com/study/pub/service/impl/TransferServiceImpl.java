package com.study.pub.service.impl;

import com.study.pub.dao.AccountDao;
import com.study.pub.dao.impl.AccountDaoImpl;
import com.study.pub.pojo.Account;
import com.study.pub.service.TransferService;

public class TransferServiceImpl implements TransferService {

      private AccountDao accountDao = new AccountDaoImpl();

      @Override
      public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {
        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);
        from.setMoney(from.getMoney()-money);
        to.setMoney(to.getMoney()+money);
        accountDao.updateAccountByCardNo(from);
        accountDao.updateAccountByCardNo(to);
      }
}
