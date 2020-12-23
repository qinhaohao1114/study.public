package com.rabbit.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class HelloWorldSender {

    private static  String QUEUE_NAME="hello";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.100.55.164");
        factory.setVirtualHost("/");
        factory.setUsername("root");
        factory.setPassword("123456");
        factory.setPort(5672);
        try (Connection conn=factory.newConnection();
             Channel channel=conn.createChannel()){
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!111";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
