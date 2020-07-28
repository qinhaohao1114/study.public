package com.zk.client;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author qinhaohao
 * @Date 2020/7/23 3:54 下午
 **/
public class CreateSession implements Watcher {

    private static CountDownLatch countDownLatch=new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.10.159:2181", 5000, new CreateSession());
        System.out.println(zooKeeper.getState());
        countDownLatch.await();
        //标识会话真正建立
        System.out.println("===Client Connect to zookeeper====");
    }

    // 当前类实现了Watcher接⼝，重写了process⽅法，该⽅法负责处理来⾃Zookeeper服务端的
    // watcher通知，在收到服务端发送过来的SyncConnected事件之后，解除主程序在CountDownLatch上
    // 的等待阻塞，⾄此，会话创建完毕
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState()==Event.KeeperState.SyncConnected){
            countDownLatch.countDown();
        }
    }
}
