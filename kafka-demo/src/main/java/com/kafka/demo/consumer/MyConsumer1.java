package com.kafka.demo.consumer;

import com.kafka.demo.KafkaConfigs;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class MyConsumer1 {

    public static void main(String[] args) {
        //创建消费者对象
        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(KafkaConfigs.getConfigsConsumer());

        Pattern pattern = Pattern.compile("topic_[0-9]");

        final List<String> topics = Arrays.asList("topic_1");

        consumer.subscribe(topics, new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                partitions.forEach(tp->{
                    System.out.println("剥夺的分区："+tp.partition());
                });
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                partitions.forEach(tp->{
                    System.out.println(tp.partition());
                });
            }
        });

        //拉取订阅主题的消息
        final ConsumerRecords<Integer, String> records = consumer.poll(3_000);
        //获取topic_1主题的消息
        final Iterable<ConsumerRecord<Integer, String>> topic1Iterable = records.records("topic_1");

        topic1Iterable.forEach(record->{
            System.out.println("========================================");
            System.out.println("消息头字段：" +
                    Arrays.toString(record.headers().toArray()));
            System.out.println("消息的key：" + record.key());
            System.out.println("消息的偏移量：" + record.offset());
            System.out.println("消息的分区号：" + record.partition());
            System.out.println("消息的序列化key字节数：" +
                    record.serializedKeySize());
            System.out.println("消息的序列化value字节数：" +
                    record.serializedValueSize());
            System.out.println("消息的时间戳：" + record.timestamp());
            System.out.println("消息的时间戳类型：" + record.timestampType());
            System.out.println("消息的主题：" + record.topic());
            System.out.println("消息的值：" + record.value());
        });

        consumer.close();

    }
}
