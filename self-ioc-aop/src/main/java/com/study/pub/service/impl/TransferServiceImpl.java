package com.study.pub.service.impl;

import com.study.pub.dao.AccountDao;
import com.study.pub.dao.impl.AccountDaoImpl;
import com.study.pub.factory.BeanFactory;
import com.study.pub.pojo.Account;
import com.study.pub.service.TransferService;
import com.study.pub.utils.TransactionManager;

public class TransferServiceImpl implements TransferService {

      private AccountDao accountDao;

//      private TransactionManager transactionManager;

      @Override
      public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {
//          transactionManager.beginTransaction();
//          try {
              Account from = accountDao.queryAccountByCardNo(fromCardNo);
              Account to = accountDao.queryAccountByCardNo(toCardNo);
              from.setMoney(from.getMoney()-money);
              to.setMoney(to.getMoney()+money);
              accountDao.updateAccountByCardNo(from);
              accountDao.updateAccountByCardNo(to);
//              transactionManager.commitTransaction();
//          }catch (Exception e){
//              transactionManager.rollbackTransaction();
//              throw e;
//          }
      }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

//    public void setTransactionManager(TransactionManager transactionManager) {
//        this.transactionManager = transactionManager;
//    }
}
