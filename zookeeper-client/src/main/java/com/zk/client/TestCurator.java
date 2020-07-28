package com.zk.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @Author qinhaohao
 * @Date 2020/7/23 5:40 下午
 **/
public class TestCurator {

    public static void main(String[] args) throws Exception {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
//        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.159:2181", retry);
//        client.start();

        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.159:2181", 5000,1000,retry);
        client.start();
        System.out.println("Zookeeper session1 established. ");

        CuratorFramework clent1 = CuratorFrameworkFactory.builder().connectString("192.168.10.159:2181").sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(retry)
                .namespace("base")
                .build();
        clent1.start();

        System.out.println("Zookeeper session2 established. ");
        String path="qhh";
        client.create().forPath(path);

        client.create().forPath(path+1,"我是内容".getBytes("utf-8"));

        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);


    }
}
