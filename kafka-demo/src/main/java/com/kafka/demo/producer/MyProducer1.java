package com.kafka.demo.producer;

import com.kafka.demo.KafkaConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.concurrent.TimeUnit;

public class MyProducer1 {

    public static void main(String[] args) throws Exception {
        KafkaProducer<Integer, String> producer = new KafkaProducer<>(KafkaConfigs.getConfigsWithAck());

        //用于封装producer的消息
        ProducerRecord<Integer, String> record = new ProducerRecord<>(
                "topic_1",//主题名称
                0,//分区编号，现在只有一个分区，所以是0
                0, //数字作为key
                "qhh message 0" //字符串作为value
        );
        //发送消息，同步等待消息确认
        producer.send(record).get(3000, TimeUnit.MILLISECONDS);

        //生产者关闭
        producer.close();
    }
}
