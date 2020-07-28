package com.dubbo.consumer;

import com.dubbo.service.HelloService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Author qinhaohao
 * @Date 2020/7/28 4:37 下午
 **/
public class XmlConsumerMain {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:dubbo-consumer.xml");
        context.start();
        while (true){
            System.in.read();
            HelloService helloService = context.getBean(HelloService.class);
            System.out.println(helloService.sayHello("word"));
        }

    }
}
