package com.rabbit.queue;

import com.rabbit.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

public class Worker {
    private static final String TASK_QUEUE_NAME = "taskQueue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // true表示不需要手动确认消息，false表示需要手动确认消息：channel.basicAck(xxx, yyy);
        boolean autoAck=true;
        channel.exchangeDeclare("logs", "fanout");
        channel.queueBind(TASK_QUEUE_NAME,"logs","taskQueue");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String task = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + task + "'");
            try {
                doWork(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(" [x] Done");
                // 手动确认消息
//                 channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };
        channel.queueDeclare(TASK_QUEUE_NAME,false,false,false,null);
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
        });
    }

    private static void doWork(String task) throws InterruptedException{
        System.out.println("task = "+task);
        for (char ch : task.toCharArray()) {
            if (ch=='.') Thread.sleep(1000);
        }
    }


}
