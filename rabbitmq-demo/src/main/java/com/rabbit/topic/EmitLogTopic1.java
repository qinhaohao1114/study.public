package com.rabbit.topic;

import com.rabbit.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Random;

public class EmitLogTopic1 {

    private static final String EXCHANGE_NAME = "topic_logs";

    private static final String[] SPECIES = {
            "dog",
            "rabbit",
            "chicken",
            "horse",
            "bear",
            "cat"
    };

    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String message = null;
        String routingKey = null;
        String speed;
        String species;
        for (int i = 0; i < 10; i++) {
            speed = "lazy";
            species = SPECIES[RANDOM.nextInt(SPECIES.length)];
            message = speed + "-" + species;
            routingKey = speed + "." + species;
            channel.basicPublish(EXCHANGE_NAME, routingKey, null,
                    message.getBytes());
        }
        System.out.println(" [x] Sent '" + routingKey + "':'" + message +
                "'");
    }
}
