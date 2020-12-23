package com.rabbit.topic;

import com.rabbit.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Random;

public class EmitLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    private static final String[] SPEED = {
            "lazy",
            "quick",
            "normal"
    };
    private static final String[] COLOR = {
            "black",
            "orange",
            "red",
            "yellow",
            "blue",
            "white",
            "pink"
    };
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
        String color;
        String species;
        for (int i = 0; i < 10; i++) {
            speed = SPEED[RANDOM.nextInt(SPEED.length)];
            color = COLOR[RANDOM.nextInt(COLOR.length)];
            species = SPECIES[RANDOM.nextInt(SPECIES.length)];
            message = speed + "-" + color + "-" + species;
            routingKey = speed + "." + color + "." + species;
            channel.basicPublish(EXCHANGE_NAME, routingKey, null,
                    message.getBytes());
        }
        System.out.println(" [x] Sent '" + routingKey + "':'" + message +
                "'");
    }
}
