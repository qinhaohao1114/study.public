package com.rabbitmq.spring.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;

@Configuration
public class RabbitConfiguration {

    @Bean
    public com.rabbitmq.client.ConnectionFactory rabbitFactory() {
        com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
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
        return factory;
    }

    @Bean
    public ConnectionFactory connectionFactory(com.rabbitmq.client.ConnectionFactory rabbitFactory) {
        return new CachingConnectionFactory(rabbitFactory);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("myExchange");
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with("qhh").noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory, Queue queue, Exchange exchange) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setExchange(exchange.getName());
        rabbitTemplate.setDefaultReceiveQueue(queue.getName());
        rabbitTemplate.setRoutingKey("qhh");
        return rabbitTemplate;
    }

    @Bean
    public Queue queue() {
        return new Queue("myqueue");
    }

    /**
     * NONE模式，则只要收到消息后就立即确认（消息出列，标记已消费），有丢失数据的风险
     * AUTO模式，看情况确认，如果此时消费者抛出异常则消息会返回到队列中
     * MANUAL模式，需要显式的调用当前channel的basicAck方法
     * @param channel
     * @param deliveryTag
     * @param message
     */
    @RabbitListener(queues = "lagou.topic.queue", ackMode = "AUTO")
    public void handleMessageTopic(Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, @Payload byte[]
                                           message) {
        System.out.println("RabbitListener消费消息，消息内容：" + new
                String((message)));
        try {
           // 手动ack，deliveryTag表示消息的唯一标志，multiple表示是否是批量确认
            channel.basicAck(deliveryTag, false);
            // 手动nack，告诉broker消费者处理失败，最后一个参数表示是否需要将消息重新入列
            channel.basicNack(deliveryTag, false, true);
            // 手动拒绝消息。第二个参数表示是否重新入列
            channel.basicReject(deliveryTag, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
