package com.dubbo.provider.impl;

import com.dubbo.service.HelloService;

import java.util.concurrent.TimeUnit;

/**
 * @Author qinhaohao
 * @Date 2020/7/28 4:14 下午
 **/
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello："+name;
    }
}
