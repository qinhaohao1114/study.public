package com.kafka.demo.producer;

import com.kafka.demo.KafkaConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class MyProducer3 {

    public static void main(String[] args) throws Exception {

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(KafkaConfigs.getConfigsProducer());

        for (int i = 100; i < 200; i++) {
            //用于封装producer的消息
            ProducerRecord<Integer, String> record = new ProducerRecord<>(
                    "topic_1",//主题名称
                    0,//分区编号，现在只有一个分区，所以是0
                    i, //数字作为key
                    "qhh message "+i //字符串作为value
            );
            //使用回调异步等待消息的确认
            producer.send(record, (metadata, e) -> {
                if (e==null){
                    System.out.println(
                            "主题：" + metadata.topic() + "\n"
                                    + "分区：" + metadata.partition() + "\n"
                                    + "偏移量：" + metadata.offset() + "\n"
                                    + "序列化的key字节：" +
                                    metadata.serializedKeySize() + "\n"
                                    + "序列化的value字节：" +
                                    metadata.serializedValueSize() + "\n"
                                    + "时间戳：" + metadata.timestamp()
                    );
                }else {
                    System.out.println("有异常："+e.getMessage());
                }
            });
        }

        //生产者关闭
        producer.close();
    }
}
