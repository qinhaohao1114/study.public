package com.dubbo.consumer;

import com.dubbo.consumer.service.ConsumerComponent;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

/**
 * @Author qinhaohao
 * @Date 2020/7/24 3:11 下午
 **/
public class AnnotationConsumerMain {

    public static void main(String[] args) throws Exception {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(consumerConfiguration.class);
        context.start();
        ConsumerComponent service = context.getBean(ConsumerComponent.class);
        while (true){
            System.in.read();
            try {
                String word = service.sayHello("word");
                System.out.println("result: "+word);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Configuration
    @EnableDubbo(scanBasePackages = "com.dubbo.consumer.service")
    @PropertySource("classpath:/dubbo-consumer.properties")
    @ComponentScan(value = {"com.dubbo.consumer"})
    static class consumerConfiguration{

    }
}
