package com.rocketmq.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MyPushConsumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("qhhtest_grp_01");
        consumer.setNamesrvAddr("rmq-namesrv.rocketmq.svc.cluster.local:9876");
        consumer.start();

        consumer.subscribe("qhh_test_02","*");

        consumer.setMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageQueue messageQueue = context.getMessageQueue();
                System.out.println(messageQueue);
                for (MessageExt msg : msgs) {
                    try {
                        System.out.println(new String(msg.getBody(),"utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();

    }
}
