package com.study.pub.dao.impl;

import com.study.pub.dao.AccountDao;
import com.study.pub.pojo.Account;
import com.study.pub.utils.ConnectionUtils;
import com.study.pub.utils.DruidUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Author qinhaohao
 * @Date 2020-05-30 11:25
 **/
public class AccountDaoImpl implements AccountDao {

    private ConnectionUtils connectionUtils;
    @Override
    public Account queryAccountByCardNo(String cardNo) throws Exception {
        Connection con = connectionUtils.getCurrentThreadConn();
        String sql = "select * from account where cardNo=?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1,cardNo);
        ResultSet resultSet = preparedStatement.executeQuery();
        Account account = new Account();
        while(resultSet.next()) {
            account.setCardNo(resultSet.getString("cardNo"));
            account.setName(resultSet.getString("name"));
            account.setMoney(resultSet.getInt("money"));
        }
        resultSet.close();
        preparedStatement.close();
        return account;
    }

    @Override
    public int updateAccountByCardNo(Account account) throws Exception {
        Connection con = connectionUtils.getCurrentThreadConn();
        String sql = "update account set money=? where cardNo=?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setInt(1,account.getMoney());
        preparedStatement.setString(2,account.getCardNo());
        int i = preparedStatement.executeUpdate();
        preparedStatement.close();
        return i;
    }

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }
}
