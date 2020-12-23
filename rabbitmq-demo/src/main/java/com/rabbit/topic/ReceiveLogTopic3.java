package com.rabbit.topic;

import com.rabbit.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class ReceiveLogTopic3 {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> System.out.println("*.*.rabbit 匹配到的消息：" + new String(delivery.getBody(), StandardCharsets.UTF_8));
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }

}
