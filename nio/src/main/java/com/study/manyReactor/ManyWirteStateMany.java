package com.study.manyReactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class ManyWirteStateMany implements ManyHandlerState {
    @Override
    public void changeState(TCPManyHandler h) {
        h.setState(new ManyReadStateMany());
    }

    @Override
    public void handle(TCPManyHandler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {
        String str = "Your message has sent to "
                + sc.socket().getLocalSocketAddress().toString() + "\r\n";
        ByteBuffer buf = ByteBuffer.wrap(str.getBytes()); // wrap自動把buf的position設為0，所以不需要再flip()

        while (buf.hasRemaining()) {
            sc.write(buf); // 回傳給client回應字符串，發送buf的position位置 到limit位置為止之間的內容
        }
        h.setState(new ManyReadStateMany()); // 改變狀態(SENDING->READING)
        sk.interestOps(SelectionKey.OP_READ); // 通過key改變通道註冊的事件
        sk.selector().wakeup(); // 使一個阻塞住的selector操作立即返回
    }
}
