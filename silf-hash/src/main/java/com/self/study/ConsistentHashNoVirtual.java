package com.self.study;

import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashNoVirtual {

    public static void main(String[] args) {
        //step1 初始化：把服务器节点IP的哈希值对应到哈希环上
       // 定义服务器ip
        String[] tomcatServers = new String[]
                {"123.111.0.0","123.101.3.1","111.20.35.2","123.98.26.3"};

        SortedMap<Integer,String> hashServerMap=new TreeMap<>();

        for (String tomcatServer : tomcatServers) {
            int serverHash = Math.abs(tomcatServer.hashCode());
            hashServerMap.put(serverHash,tomcatServer);
        }

        String[] clients = {"10.78.12.3", "113.25.63.1", "126.12.3.8"};

        for (String client : clients) {
            int clientHash = Math.abs(client.hashCode());

            SortedMap<Integer, String> integerStringSortedMap = hashServerMap.tailMap(clientHash);
            if (integerStringSortedMap.isEmpty()){
                Integer firstKey = hashServerMap.firstKey();
                System.out.println("====>>>客户端: "+client+" 被路由到服务器: "+hashServerMap.get(firstKey));
            }else {
                Integer firstKey = integerStringSortedMap.firstKey();
                System.out.println("====>>>客户端: "+client+" 被路由到服务器: "+integerStringSortedMap.get(firstKey));
            }
        }
    }
}
