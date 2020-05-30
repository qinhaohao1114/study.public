package com.study.pub.utils;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author 应癫
 */
public class DruidUtils {

    private DruidUtils(){
    }

    private static DruidDataSource druidDataSource = new DruidDataSource();


    static {
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://47.100.55.164:3306/test");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("qhh921114");

    }

    public static DruidDataSource getInstance() {
        return druidDataSource;
    }

}
