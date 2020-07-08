package com.self.study;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author qinhaohao
 * @Date 2020/7/8 3:32 下午
 *      * 1、实现需求
 *      * 1、目前有10个服务端，一个客户端，客户端发送一个写请求，通过选举选出1个服务端执行该请求，其他服务端通过复制来进行同步
 *      * 2、选举出来之后通知所有客户端
 *      * 3、角色划分
 *      *   acceptor 5个
 *      *   proposer 5
 *      *   learners 10个
 * paxosMain
 **/
public class PaxosMain {

    public List<ServerAcceptor> acceptors;

    public List<ServerPaxos> proposers;

    public List<ServerPaxos> learners;

    public AtomicInteger index=new AtomicInteger();

    public List<String> ips;

    public boolean initFlag=false;

    public static String RETRY="retry";

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new PaxosMain().write("测试写入"+i);
        }

//        System.out.println(random);
    }


    public void init(){

        ServerPaxos paxos1 = new ServerPaxos("192.168.0.1");
        ServerPaxos paxos2 = new ServerPaxos("192.168.0.2");
        ServerPaxos paxos3 = new ServerPaxos("192.168.0.3");
        ServerPaxos paxos4 = new ServerPaxos("192.168.0.4");
        ServerPaxos paxos5 = new ServerPaxos("192.168.0.5");
        ServerPaxos paxos6 = new ServerPaxos("192.168.0.6");
        ServerPaxos paxos7 = new ServerPaxos("192.168.0.7");
        ServerPaxos paxos8 = new ServerPaxos("192.168.0.8");
        ServerPaxos paxos9 = new ServerPaxos("192.168.0.9");
        ServerPaxos paxos10 = new ServerPaxos("192.168.0.10");
//        ips=Lists.newArrayList("192.168.0.1","192.168.0.2","192.168.0.3","192.168.0.4","192.168.0.5","192.168.0.6","192.168.0.7","192.168.0.8","192.168.0.9","192.168.0.10");
        acceptors= Lists.newArrayList(new ServerAcceptor(paxos1),new ServerAcceptor(paxos3),new ServerAcceptor(paxos5),new ServerAcceptor(paxos7),new ServerAcceptor(paxos9));
        proposers= Lists.newArrayList(paxos2,paxos4,paxos6,paxos8,paxos10);
        learners=Lists.newArrayList(paxos1,paxos3,paxos5,paxos7,paxos9,paxos2,paxos4,paxos6,paxos8,paxos10);
        initFlag=true;
    }


   /**
    * @Author qinhaohao
    * @Description //写入方法
    * @Date 3:44 下午 2020/7/8
    * @Param [content]
    * @return void
    **/
    public void write(String content){
        ServerPaxos writeServer = getWriteServer();
        System.out.println(writeServer.getIp()+"获得写入权限");
        writeServer.write(content);
    }

    /**
     * @Author qinhaohao
     * @Description //获取写入客户端
     * @Date 3:45 下午 2020/7/8
     * @Param []
     * @return com.self.study.PaxosMain.ServerPaxos
     **/
    public ServerPaxos getWriteServer(){
        if (!initFlag){
            init();
        }
        String result = null;
        while (result==null){

            AtomicInteger count = new AtomicInteger();
            publishProposer(count);
            while (count.get()<proposers.size()){

            }
            result=checkOver();
        }
        for (ServerPaxos proposer : proposers) {
            if (proposer.getIp().equals(result)){
                return proposer;
            }
        }
        return null;
    }

    /**
     * @Author qinhaohao
     * @Description //发送提案
     * @Date 5:22 下午 2020/7/8
     * @Param [count]
     * @return void
     **/
    public void publishProposer(AtomicInteger count){
        for (int i = 0; i < proposers.size(); i++) {
            CompletableFuture.runAsync(() -> {
//                System.out.println("执行提案");
                int random=(int)(Math.random()*5+1);
                ServerPaxos paxos = proposers.get(random-1);
                //申请的提案编号index不一定能用 半数响应之后才能用
                int index = getIndex();
                String value = getValue(index);
                while (RETRY.equals(value)){
                    System.out.println("重新申请提案");
                    value = getValue(index);
                }
                if (value==null){
                    value=paxos.getIp();
                }
                //沉睡模拟网络延时，多线程执行，到达时间不一致
                try {
                    Thread.sleep(random * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                System.out.println("沉睡完毕");
                count.incrementAndGet();
                acceptorMessage(new Proposal(index, value));
            });
        }
    }


    //校验投票结束
    public String checkOver(){
        HashMap<String, Integer> count = new HashMap<>();
        for (ServerAcceptor acceptor : acceptors) {
            Integer c = count.getOrDefault(acceptor.getPort(), 0);
            c++;
            count.put(acceptor.getPort(),c);
        }
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            if (entry.getValue()>acceptors.size()/2){
                return entry.getKey();
            }
        }
        return null;
    }

    //接受提案
    public void acceptorMessage(Proposal proposal){
        for (int i = 0; i < acceptors.size() / 2; i++) {
            int random=(int)(Math.random()*5+1);
            ServerAcceptor acceptor=acceptors.get(random);
            //编号小于等于，忽略不计
            if (proposal.getIndex()<=acceptor.getPrepareIndex()){
                continue;
            }
            //接受提案
            acceptor.setPort(proposal.getValue());
            acceptor.setPrepareIndex(proposal.getIndex());
        }

    }
    /**
     * @Author qinhaohao
     * @Description //申请提案
     * @Date 4:09 下午 2020/7/8
     * @Param []
     * @return java.lang.String
     **/
    public String getValue(int index){
        //获取当前index最大的值
        String result=null;
        Integer max=0;
        int acceptCount=0;
        for (ServerAcceptor acceptor : acceptors) {
            //如果当前的提案编号小，则忽略
            if (acceptor.getPrepareIndex()>=index){
                continue;
            }
            acceptCount++;
            if (acceptor.getPrepareIndex()>max){
                result=acceptor.getPort();
                max=acceptor.getPrepareIndex();
            }
        }
        //如果半数响应过，则可用，否则重试
        if (acceptCount>acceptors.size()/2){
            return result;
        }
        return RETRY;

//        HashMap<String, Integer> count = new HashMap<>();
//        for (ServerAcceptor acceptor : acceptors) {
//            if (acceptor.getPort()==null||acceptor.getPort().isEmpty()){
//                continue;
//            }
//            Integer c = count.getOrDefault(acceptor.getPort(), 0);
//            c++;
//            count.put(acceptor.getPort(),c);
//        }
//        if (count.isEmpty()){
//            return null;
//        }
//        String result=null;
//        Integer max=0;
//        for (Map.Entry<String, Integer> entry : count.entrySet()) {
//            if (entry.getValue()>max){
//                result=entry.getKey();
//                max=entry.getValue();
//            }
//        }
//        return result;
    }

    public int getIndex(){
       return index.incrementAndGet();
    }


    /**
     * @Author qinhaohao
     * @Description //提案类
     * @Date 4:27 下午 2020/7/8
     * @Param
     * @return
     **/
    class Proposal{
        private Integer index;

        private String value;

        public Proposal(Integer index, String value) {
            this.index = index;
            this.value = value;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    class ServerAcceptor{

        private ServerPaxos serverPaxos;

        //接收的提案
        private String port;

        //序号
        private int prepareIndex;

        public ServerAcceptor(ServerPaxos serverPaxos) {
            this.serverPaxos = serverPaxos;
        }

        public ServerPaxos getServerPaxos() {
            return serverPaxos;
        }

        public void setServerPaxos(ServerPaxos serverPaxos) {
            this.serverPaxos = serverPaxos;
        }

        public synchronized String getPort() {
            return port;
        }

        public synchronized void setPort(String port) {
            this.port = port;
        }

        public synchronized int getPrepareIndex() {
            return prepareIndex;
        }

        public synchronized void setPrepareIndex(int prepareIndex) {
            this.prepareIndex = prepareIndex;
        }
    }

    class ServerPaxos{

        private String ip;
        //需要进行复制的server
        private List<ServerPaxos> listeners=new ArrayList<>();

        public ServerPaxos(String ip) {
            this.ip = ip;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public void write(String content){
            System.out.println("执行写入:===>"+content);
            copy(content);
        }

        public void copy(String content){
            if (listeners.isEmpty()){
                return;
            }
            System.out.println("执行复制:===>"+content);
            for (ServerPaxos listener : listeners) {
                listener.write(content);
            }
        }

        public void addListener(ServerPaxos serverPaxos){
            listeners.add(serverPaxos);
        }

        public void removeListener(ServerPaxos serverPaxos){
            listeners.remove(serverPaxos);
        }

    }


}
