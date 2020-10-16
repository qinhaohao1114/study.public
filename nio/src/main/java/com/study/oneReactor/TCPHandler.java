package com.study.oneReactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class TCPHandler implements Runnable{

    private final SelectionKey sk;
    private final SocketChannel sc;
    int state;

    public TCPHandler(SelectionKey sk, SocketChannel sc) {
        this.sk=sk;
        this.sc=sc;
        state=0;//初始状态设置为reading
    }

    @Override
    public void run() {
        try {
            if (state==0){
                read();
            }else {
                write();
            }
        }catch (Exception e){
            e.printStackTrace();
            closeChannel();
        }

    }
    private void closeChannel() {
        try {
            sk.cancel();
            sc.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void read() throws IOException {
        byte[] bytes = new byte[1024];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int read = sc.read(buffer);
        if (read==-1){
            System.out.println("[Warning!] A client has been closed.");
            closeChannel();
            return;
        }
        String str = new String(bytes);
        if (str!=null&&!str.equals("")){
            process(str);
            System.out.println(sc.socket().getRemoteSocketAddress().toString()
                    + " > " + str);
            this.state=1;//改变状态
            sk.interestOps(SelectionKey.OP_WRITE);
            sk.selector().wakeup();
        }

    }

    private void process(String str) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void write() throws IOException {
        String str = "Your message has sent to "
                + sc.socket().getLocalSocketAddress().toString() + "\r\n";
        ByteBuffer buf = ByteBuffer.wrap(str.getBytes()); // wrap自動把buf的position設為0，所以不需要再flip()

        while (buf.hasRemaining()) {
            sc.write(buf); // 回傳給client回應字符串，發送buf的position位置 到limit位置為止之間的內容
        }
        state = 0; // 改變狀態
        sk.interestOps(SelectionKey.OP_READ); // 通過key改變通道註冊的事件
        sk.selector().wakeup(); // 使一個阻塞住的selector操作立即返回
    }

}
