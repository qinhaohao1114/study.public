package com.zk.client;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author qinhaohao
 * @Date 2020/7/23 4:13 下午
 **/
public class GetNodeData implements Watcher {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws Exception {
        zooKeeper = new ZooKeeper("192.168.10.159:2181", 5000, new CreateSession());
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
       //⼦节点列表发⽣变化时，服务器会发出NodeChildrenChanged通知，但不会把变化情况告诉给客户端
       // 需要客户端⾃⾏获取，且通知是⼀次性的，需反复注册监听
        if (watchedEvent.getType()==Event.EventType.NodeChildrenChanged){
            try {
                List<String> children = zooKeeper.getChildren(watchedEvent.getPath(), true);
                System.out.println(children);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (watchedEvent.getState()==Event.KeeperState.SyncConnected){
            try {
                getNodeData();
                getChildrens();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void getChildrens() throws Exception {
        /*
         path:路径
         watch:是否要启动监听，当⼦节点列表发⽣变化，会触发监听
         zooKeeper.getChildren(path, watch);
         */
        List<String> children = zooKeeper.getChildren("/lg_persistent", true);
        System.out.println(children);
    }

    private void getNodeData() throws Exception {
        /**
         * path : 获取数据的路径
         * watch : 是否开启监听
         * stat : 节点状态信息
         * null: 表示获取最新版本的数据
         * zk.getData(path, watch, stat);
         */
        byte[] data = zooKeeper.getData("/lg_persistent/lg_chidren", true, null);
        System.out.println(new String(data,"utf-8"));
    }


}
