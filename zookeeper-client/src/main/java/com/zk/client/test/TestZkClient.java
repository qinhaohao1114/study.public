package com.zk.client.test;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @Author qinhaohao
 * @Date 2020/7/23 5:21 下午
 **/
public class TestZkClient {

   public static ZkClient zkClient ;

    public static void main(String[] args) throws Exception {
       zkClient = new ZkClient("192.168.10.159:2181");
       System.out.println("ZooKeeper session established.");
//       childrenChange();
        getData();
    }

    private static void getData() throws Exception {
        final String path="/lg-zkClient-Ep";
        boolean exists = zkClient.exists(path);
        if (!exists){
            zkClient.createEphemeral(path,123);
        }
        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            public void handleDataChange(String path, Object data) throws Exception {
                System.out.println(path+"该节点内容被更新，更新后的内容"+data);
            }

            public void handleDataDeleted(String s) throws Exception {
                System.out.println(s+" 该节点被删除");
            }
        });

        Object o = zkClient.readData(path);
        System.out.println(o);

        zkClient.writeData(path,"4567");
        Thread.sleep(1000);

        zkClient.delete(path);
        Thread.sleep(1000);
    }


    private static void childrenChange() throws Exception {
        String path = "/lg-zkClient/lg-cl";
        zkClient.createPersistent(path,true);
//        zkClient.updateDataSerialized();
        System.out.println("success create znode.");
//        zkClient.deleteRecursive(path);
        List<String> children = zkClient.getChildren("/lg-zkClient");
        System.out.println(children);
        zkClient.subscribeChildChanges("/lg-zkClient", new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println(parentPath + " 's child changed, currentChilds:" + currentChilds);
            }
        });
//        zkClient.createPersistent("/lg-zkClient");
//        Thread.sleep(1000);
        zkClient.createPersistent("/lg-zkClient/c1");
        Thread.sleep(1000);
        zkClient.delete("/lg-zkClient/c1");
        Thread.sleep(1000);
        zkClient.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
