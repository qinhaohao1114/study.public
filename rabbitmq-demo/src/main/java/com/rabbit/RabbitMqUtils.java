package com.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqUtils {

    public static Channel getChannel() throws Exception {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置服务器主机名或IP地址
        factory.setHost("47.100.55.164");
        // 设置Erlang的虚拟主机名称
        factory.setVirtualHost("/");
        // 设置用户名
        factory.setUsername("root");
        // 设置密码
        factory.setPassword("123456");
        // 设置客户端与服务器的通信端口，默认值为5672
        factory.setPort(5672);
        // 获取连接
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
}
