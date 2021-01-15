package com.kafka.demo.self.producer;

import com.kafka.demo.entity.User;
import com.kafka.demo.self.serializer.UserSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

public class MyProducer {

    public static void main(String[] args) {
        Map<String, Object> configs = new HashMap<>();
        //设置连接Kafka的初始连接用到的服务器地址
        //如果是集群，则可以通过此初始连接发现集群中的其他broker
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "47.100.55.164:5672");
        //设置自定义分区器
        configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.kafka.demo.self.partition.MyPartitioner");
        //设置拦截器
        configs.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "com.kafka.demo.self.interceptor.InterceptorOne," + "com.kafka.demo.self.interceptor.InterceptorTwo," + "com.kafka.demo.self.interceptor.InterceptorThree");
        //设置key的序列化器
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //设置value的序列化器
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserSerializer.class);
        configs.put("acks", "all");
        configs.put("compression.type", "gzip");
        configs.put("retries", "3");
        User user = new User();
        user.setUserId(1001);
        user.setUsername("张三");
        KafkaProducer<String, User> producer = new KafkaProducer<>(configs);
        ProducerRecord<String, User> record = new ProducerRecord<>(
                "tp_user_01",
                0,
                user.getUsername(),
                user
        );
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println(
                        "消息发送成功：" + metadata.topic() + "\t"
                                + metadata.partition() + "\t"
                                + metadata.offset() + "\t");
            } else {
                System.out.println("消息发送异常");
            }
        });

        producer.close();
    }
}
