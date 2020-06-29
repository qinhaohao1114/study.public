package com.self.study;

public class GeneralHash {

    public static void main(String[] args) {
        String[] clients = {"10.78.12.3", "113.25.63.1", "126.12.3.8"};

        int serverCount=5;
        //hash(ip)%nodeCounts=index
        //根据index锁定应该路由到的tomcat服务器
        for (String client : clients) {
            int hash = Math.abs(client.hashCode());
            int index = hash % serverCount;
            System.out.println("客户端:"+client+" 被路由到服务器编号为："+index);
        }
    }
}
