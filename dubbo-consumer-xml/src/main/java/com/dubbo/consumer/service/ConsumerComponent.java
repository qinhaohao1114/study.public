package com.dubbo.consumer.service;

import com.dubbo.service.HelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author qinhaohao
 * @Date 2020/7/28 4:36 下午
 **/
@Component
public class ConsumerComponent {

        @Autowired
        private HelloService helloService;

        public String sayHello(String name) {
            return helloService.sayHello(name);
        }
}
