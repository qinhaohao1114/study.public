package com.rabbitmq.spring;

import com.rabbitmq.spring.config.RabbitConfiguration;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class App {

    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(RabbitConfiguration.class);
        AmqpTemplate template = context.getBean(AmqpTemplate.class);
        template.convertAndSend("qhh","foo");
        String foo= (String) template.receiveAndConvert("myqueue");
        System.out.println(foo);
        context.close();
    }

    private void test(){

    }
}
