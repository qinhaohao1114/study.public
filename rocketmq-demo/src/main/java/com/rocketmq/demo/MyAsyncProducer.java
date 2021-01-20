package com.rocketmq.demo;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class MyAsyncProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("qhhtest_grp_01");
        producer.setNamesrvAddr("rmq-namesrv.rocketmq.svc.cluster.local:9876");
        producer.start();
        for (int i = 0; i < 100; i++) {
            Message message = new Message("qhh_test_01", ("hello qhh " + i).getBytes("UTF-8"));
            producer.send(message, new SendCallback() {
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送成功 "+sendResult);
                }

                public void onException(Throwable throwable) {
                    System.out.println("发送失败： "+throwable.getMessage());
                }
            });
        }
        Thread.sleep(10000);

        producer.shutdown();
    }
}
