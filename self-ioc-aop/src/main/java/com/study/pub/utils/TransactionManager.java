package com.study.pub.utils;

import java.sql.SQLException;

/**
 * @Author qinhaohao
 * @Date 2020-05-30 13:37
 **/
public class TransactionManager {

    private ConnectionUtils connectionUtils;


    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    public void beginTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }

    public void commitTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().commit();
    }

    public void rollbackTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().rollback();
    }
}
