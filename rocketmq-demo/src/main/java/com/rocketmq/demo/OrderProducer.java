package com.rocketmq.demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class OrderProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("qhhtest_grp_01");
        producer.setNamesrvAddr("rmq-namesrv.rocketmq.svc.cluster.local:9876");
        producer.start();
        List<MessageQueue> queues = producer.fetchPublishMessageQueues("qhh_test_01");
        System.out.println(queues.size());
        Message message=null;
        MessageQueue queue=null;
        for (int i = 0; i < 100; i++) {
            queue = queues.get(i % 8);
            message = new Message("qhh_test_01", ("hello lagou - order create" + i).getBytes());
                    producer.send(message, queue);
            message = new Message("qhh_test_01", ("hello lagou - order payed" + i).getBytes());
                    producer.send(message, queue);
            message = new Message("qhh_test_01", ("hello lagou - order ship" + i).getBytes());
            producer.send(message, queue);
        }
        producer.shutdown();
    }

}
