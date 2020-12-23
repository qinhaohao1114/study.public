package com.rabbit.queue;

import com.rabbit.RabbitMqUtils;
import com.rabbit.fanout.EmitLog;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class NewTask {

    private static String QUEUE_NAME = "";
    private static final String[] works = {
            "hello.",
            "hello..",
            "hello...",
            "hello....",
            "hello.....",
            "hello......",
            "hello.......",
            "hello........",
            "hello.........",
            "hello.........."};

    public static void main(String[] args) throws Exception {
        confirm();
//        Channel channel = RabbitMqUtils.getChannel();
//        channel.exchangeDeclare("logs", "fanout");
//        channel.queueDeclare("taskQueue", false, false, false, null);
//        for (String work : works) {
//            // BuiltinExchangeType.DIRECT
//            // BuiltinExchangeType.FANOUT
//            // BuiltinExchangeType.HEADERS
//            // BuiltinExchangeType.TOPIC
//            // channel.exchangeDeclare("ex1",BuiltinExchangeType.DIRECT);
//            // 将消息路由到taskQueue队列
//            channel.basicPublish("logs", "taskQueue", null, work.getBytes(StandardCharsets.UTF_8));
//            // channel.basicPublish("", "hello",MessageProperties.PERSISTENT_TEXT_PLAIN, work.getBytes());
//            System.out.println("[x] sent '" + work + "'");
//        }
    }

    private static void confirm() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect();
        channel.exchangeDeclare("logs", "fanout");
        String message="hello";
        channel.queueDeclare("taskQueue", false, false, false, null);
        channel.basicPublish("logs", "taskQueue", null, message.getBytes());
        try {
            channel.waitForConfirmsOrDie(5_000);
            System.out.println("消息被确认：message = " + message);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("消息被拒绝！ message = " + message);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("在不是Publisher Confirms的通道上使用该方法");
        } catch (TimeoutException e) {
            e.printStackTrace();
            System.err.println("等待消息确认超时！ message = " + message);
        }
    }


}
