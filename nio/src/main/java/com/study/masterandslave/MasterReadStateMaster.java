package com.study.masterandslave;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

public class MasterReadStateMaster implements MasterHandlerState {

    @Override
    public void changeState(TCPMasterHandler h) {
        h.setState(new MasterWorkStateMaster());
    }

    @Override
    public void handle(TCPMasterHandler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {
        byte[] bytes = new byte[1024];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int read = sc.read(buffer);
        if (read == -1) {
            System.out.println("[Warning!] A client has been closed.");
            h.closeChannel();
            return;
        }
        String str = new String(bytes);
        if (!str.equals("")){
            System.out.println(sc.socket().getRemoteSocketAddress().toString()
                    + " > " + str);
            CountDownLatch countDownLatch=new CountDownLatch(1);
            pool.execute(()->{
                try {
                    System.out.println("开始执行业务逻辑");
                    Thread.sleep(2000);
                    System.out.println("执行完业务逻辑");
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            h.setState(new MasterWirteStateMaster());
            sk.interestOps(SelectionKey.OP_WRITE);
            sk.selector().wakeup();
        }
    }
}