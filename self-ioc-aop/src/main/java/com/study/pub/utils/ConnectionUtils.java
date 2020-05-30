package com.study.pub.utils;

import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author qinhaohao
 * @Date 2020-05-30 13:34
 **/
public class ConnectionUtils {


    private ThreadLocal<Connection> threadLocal=new ThreadLocal<>();



    public Connection getCurrentThreadConn() throws SQLException {

        Connection connection = threadLocal.get();
        if (connection==null){
            connection = DruidUtils.getInstance().getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }
}
