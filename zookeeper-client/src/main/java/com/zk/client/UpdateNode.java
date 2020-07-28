package com.zk.client;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @Author qinhaohao
 * @Date 2020/7/23 4:21 下午
 **/
public class UpdateNode implements Watcher {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws Exception {
        zooKeeper = new ZooKeeper("192.168.10.159:2181", 5000, new UpdateNode());
        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent watchedEvent) {

        if (watchedEvent.getState()==Event.KeeperState.SyncConnected){
            try {
                createNodeSync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (watchedEvent.getType()==Event.EventType.NodeCreated){
            String path = watchedEvent.getPath();
            try {
                byte[] data = zooKeeper.getData(path, false, null);
                System.out.println("创建的节点:"+path+" 值为："+new String(data));
                try {
                    updateNodeSync();
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (watchedEvent.getType()==Event.EventType.NodeDataChanged){
            try {
                byte[] data = zooKeeper.getData("/lg_persistent", false, null);
                System.out.println("修改后的值:"+new String(data));
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void createNodeSync() throws Exception {
        /**
         * path ：节点创建的路径
         * data[] ：节点创建要保存的数据，是个byte类型的
         * acl ：节点创建的权限信息(4种类型)
         * ANYONE_ID_UNSAFE : 表示任何⼈
         * AUTH_IDS ：此ID仅可⽤于设置ACL。它将被客户机验证的ID替
         换。
         * OPEN_ACL_UNSAFE ：这是⼀个完全开放的ACL(常⽤)-->
         world:anyone
         * CREATOR_ALL_ACL ：此ACL授予创建者身份验证ID的所有权限
         * createMode ：创建节点的类型(4种类型)
         * PERSISTENT：持久节点
         * PERSISTENT_SEQUENTIAL：持久顺序节点
         * EPHEMERAL：临时节点
         * EPHEMERAL_SEQUENTIAL：临时顺序节点
         String node = zookeeper.create(path,data,acl,createMode);
         */
        String node_PERSISTENT = zooKeeper.create("/lg_persistent", "持久节点内容".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        String node_PERSISTENT_SEQUENTIAL = zooKeeper.create("/lg_persistent_sequential", "持久节点内容".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        String node_EP = zooKeeper.create("/lg_ephemeral", "临时节点内容".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("创建的持久节点是:"+node_PERSISTENT);
        System.out.println("创建的持久顺序节点是:"+node_PERSISTENT_SEQUENTIAL);
        System.out.println("创建的临时节点是:"+node_EP);

    }

    private void updateNodeSync() throws Exception {
        /*
         path:路径
         data:要修改的内容 byte[]
         version:为-1，表示对最新版本的数据进⾏修改
         zooKeeper.setData(path, data,version);
         */
        byte[] data = zooKeeper.getData("/lg_persistent", false, null);
        System.out.println("修改前的值:"+new String(data));
        //修改 stat:状态信息对象 -1:最新版本
        Stat stat = zooKeeper.setData("/lg_persistent", "客户端修改内容".getBytes("utf-8"), -1);
//        byte[] data1 = zooKeeper.getData("/lg_persistent", false, null);
//        System.out.println("修改后的值:"+new String(data1));
    }
}
