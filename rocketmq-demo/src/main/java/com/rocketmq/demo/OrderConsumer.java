package com.rocketmq.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;
import java.util.Set;

public class OrderConsumer {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("qhhtest_grp_01");
        consumer.setNamesrvAddr("rmq-namesrv.rocketmq.svc.cluster.local:9876");
        consumer.start();
        Set<MessageQueue> messageQueues =
                consumer.fetchSubscribeMessageQueues("qhh_test_01");
        System.err.println(messageQueues.size());
        for (MessageQueue messageQueue : messageQueues) {
            long nextBeginOffset = 0;
            System.out.println("===============================");
            do {
                PullResult pullResult = consumer.pull(messageQueue, "*", nextBeginOffset, 1);
                if (pullResult == null || pullResult.getMsgFoundList() ==
                        null) break;
                nextBeginOffset = pullResult.getNextBeginOffset();
                List<MessageExt> msgFoundList =
                        pullResult.getMsgFoundList();
                System.out.println(messageQueue.getQueueId() + "\t" +
                        msgFoundList.size());
                for (MessageExt messageExt : msgFoundList) {
                    System.out.println(
                            messageExt.getTopic() + "\t" +
                                    messageExt.getQueueId() + "\t" +
                                    messageExt.getMsgId() + "\t" +
                                    new String(messageExt.getBody())
                    );
                }
            } while (true);
        }

    }
}
