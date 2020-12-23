package com.rabbit.ttl;

import com.rabbit.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

public class RabbitMqTTL {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 设置队列属性
        Map<String,Object> arguments = new HashMap<>();
        // 设置队列的TTL
        arguments.put("x-message-ttl",30000);
        // 设置队列的空闲存活时间（如该队列根本没有消费者，一直没有使用，队列可以存活多久）
        arguments.put("x-expires",10000);
        channel.queueDeclare("ttl",false,false,false,arguments);
        channel.exchangeDeclare("ttlExchange", BuiltinExchangeType.DIRECT);
        channel.queueBind("ttl","ttlExchange","ttl");
        for (int i = 0; i < 1000; i++) {
            String message="hello world!"+i;
            //设置消息的过期时间
            channel.basicPublish("ttlExchange","ttl",new AMQP.BasicProperties().builder().expiration("30000").build(),message.getBytes());
            System.out.println(" [X] sent '"+message+"'");
        }
    }
}
