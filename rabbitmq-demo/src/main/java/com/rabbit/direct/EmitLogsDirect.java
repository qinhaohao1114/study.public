package com.rabbit.direct;

import com.rabbit.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

public class EmitLogsDirect {

    public static  final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        testConstant();
//        Channel channel = RabbitMqUtils.getChannel();
//        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
//        String servrity=null;
//        for (int i = 0; i < 100; i++) {
//            switch (i % 3){
//                case 0:
//                    servrity="info";
//                    break;
//                case 1:
//                    servrity="warn";
//                    break;
//                case 2:
//                    servrity="error";
//                    break;
//                default:
//                    System.out.println("log错误，程序退出");
//                    System.exit(-1);
//            }
//            String logStr = "这是 【" + servrity + "】 的消息";
//            channel.basicPublish(EXCHANGE_NAME,servrity,null,logStr.getBytes(StandardCharsets.UTF_8));
//        }
    }

    private static void rollBackConfirm() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect();
        channel.exchangeDeclare(EmitLogsDirect.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EmitLogsDirect.EXCHANGE_NAME, "error");
        ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
            if (multiple) {
                System.out.println("小雨等于 " + sequenceNumber + " 的消息都被确认了");
                // 获取map集合的子集
                ConcurrentNavigableMap<Long, String> headMap = outstandingConfirms.headMap(sequenceNumber, true);
                // 清空子集
                headMap.clear();
                ;
            } else {
                System.out.println(sequenceNumber + " 对应的消息被确认");
                String removed = outstandingConfirms.remove(sequenceNumber);
            }
        };
        /**
         * * @param ackCallback callback on ack
         * * @param nackCallback call on nack (negative ack)
         */
        channel.addConfirmListener(cleanOutstandingConfirms, (sequenceNumber, multiple) -> {
            if (multiple) {
                System.out.println("小于等于 " + sequenceNumber + " 的消息都被确认了");
                ConcurrentNavigableMap<Long, String> headMap = outstandingConfirms.headMap(sequenceNumber, true);
            } else {
                System.out.println(sequenceNumber + " 对应的消息被确认");
                outstandingConfirms.remove(sequenceNumber);
            }
        });
        String message = "hello ";
        String servrity = null;
        for (int i = 0; i < 1000; i++) {
            switch (i % 3){
                case 0:
                    servrity="info";
                    break;
                case 1:
                    servrity="warn";
                    break;
                case 2:
                    servrity="error";
                    break;
                default:
                    System.out.println("log错误，程序退出");
                    System.exit(-1);
            }
            long nextPublishSeqNo = channel.getNextPublishSeqNo();
            channel.basicPublish(EmitLogsDirect.EXCHANGE_NAME,servrity,null,(message + i).getBytes(StandardCharsets.UTF_8));
            System.out.println("序列号为：" + nextPublishSeqNo + "的消息已经发送了：" + (message + i));
            outstandingConfirms.put(nextPublishSeqNo, (message + i));
        }
    }

    private static void testConstant() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //持久化交换器 durable=true
        channel.exchangeDeclare("lasting_exchange",BuiltinExchangeType.DIRECT,true);
        //持久化队列 durable=true
        channel.queueDeclare("lasting_queue",true,false,false,null);
        channel.queueBind("lasting_queue","lasting_exchange","lasting");
        String message="";
        for (int i = 0; i < 1000; i++) {
            message="hello world! "+i;
            AMQP.BasicProperties.Builder builder=new AMQP.BasicProperties.Builder();
            builder.contentType("text/plain");
            //消息持久化
            builder.deliveryMode(2);
            final AMQP.BasicProperties properties = builder.build();
            channel.basicPublish("lasting_exchange","lasting",properties,message.getBytes());
        }
    }
}
