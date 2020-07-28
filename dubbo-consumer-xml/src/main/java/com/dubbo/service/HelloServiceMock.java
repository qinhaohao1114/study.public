package com.dubbo.service;

/**
 * @Author qinhaohao
 * @Date 2020/7/28 6:07 下午
 **/
public class HelloServiceMock implements HelloService{
    @Override
    public String sayHello(String name) {
        return "hello mock";
    }
}
