package com.kafka.demo;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfigs {

    public static Map<String, Object> getConfigsWithAck(){
        Map<String, Object> configs = getConfigsProducer();
        configs.put("acks", "1");
        return configs;
    }

    public static Map<String, Object> getConfigsProducer(){
        Map<String, Object> configs = new HashMap<>();
        //设置连接Kafka的初始连接用到的服务器地址
        //如果是集群，则可以通过此初始连接发现集群中的其他broker
        configs.put("bootstrap.servers","47.100.55.164:5672");
        //设置key的序列化器
        configs.put("key.serializer","org.apache.kafka.common.serialization.IntegerSerializer");
        //设置value的序列化器
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configs.put("acks", "all");
        configs.put("compression.type", "gzip");
        configs.put("retries", "3");
        return configs;
    }

    public static Map<String, Object> getConfigsConsumer(){
        Map<String, Object> configs = new HashMap<>();
        //设置连接Kafka的初始连接用到的服务器地址
        //如果是集群，则可以通过此初始连接发现集群中的其他broker
        configs.put("bootstrap.servers","47.100.55.164:5672");
        //设置key的序列化器
        configs.put("key.deserializer","org.apache.kafka.common.serialization.IntegerDeserializer");
        //设置value的序列化器
        configs.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        configs.put("group.id", "consumer.demo");
        return configs;
    }
}
