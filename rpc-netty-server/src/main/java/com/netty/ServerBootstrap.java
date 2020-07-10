package com.netty;

import com.netty.service.UserServiceImpl;

/**
 * @Author qinhaohao
 * @Date 2020/7/10 11:02 上午
 **/
public class ServerBootstrap {

    public static void main(String[] args) {
        UserServiceImpl.startServer("127.0.0.1",8990);
    }
}
