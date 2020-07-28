package com.dubbo.provider.service;

import com.dubbo.service.HelloService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @Author qinhaohao
 * @Date 2020/7/24 2:59 下午
 **/
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "hello: "+name;
    }
}
