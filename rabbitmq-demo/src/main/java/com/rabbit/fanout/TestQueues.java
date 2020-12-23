package com.rabbit.fanout;

import com.rabbit.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestQueues {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queue = channel.queueDeclare().getQueue();
        System.out.println(queue);
        channel.close();
    }
}
