package com.rocketmq.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

public class MyPullConsumer {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException, UnsupportedEncodingException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("qhhtest_grp_01");
        consumer.setNamesrvAddr("rmq-namesrv.rocketmq.svc.cluster.local:9876");
        consumer.start();

        Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues("qhh_test_01");

        for (MessageQueue messageQueue : messageQueues) {
            // 第一个参数是MessageQueue对象，代表了当前主题的一个消息队列
            // 第二个参数是一个表达式，对接收的消息按照tag进行过滤
            // 支持"tag1 || tag2 || tag3"或者 "*"类型的写法；null或者"*"表示不对消息进行tag过滤
            // 第三个参数是消息的偏移量，从这里开始消费
            // 第四个参数表示每次最多拉取多少条消息
            PullResult result = consumer.pull(messageQueue, "*", 0, 10);

            System.out.println("message******queue******"+messageQueue);
            // 获取从指定消息队列中拉取到的消息
            List<MessageExt> msgFoundList = result.getMsgFoundList();
            if (msgFoundList==null) {
                continue;
            }
            for (MessageExt messageExt : msgFoundList) {
                System.out.println(messageExt);
                System.out.println(new String(messageExt.getBody(),"utf-8"));
            }
        }
        consumer.shutdown();
    }
}
