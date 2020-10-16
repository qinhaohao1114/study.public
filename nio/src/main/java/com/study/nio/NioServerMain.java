package com.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class NioServerMain {

    public static void main(String[] args) {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start() throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        socketChannel.bind(new InetSocketAddress(9087));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            if (selector.select(2000)==0){
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectionKeys) {
                if (selectionKey.isAcceptable()){
                    System.out.println("有人来连接");
                    SocketChannel clientChannel = socketChannel.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector,SelectionKey.OP_READ);
                }
                if (selectionKey.isReadable()){
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    channel.read(byteBuffer);
                    System.out.println("服务端收到:"+new String(byteBuffer.array()));
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                    selector.wakeup();
                }
                if (selectionKey.isWritable()){
                    SocketChannel channel= (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    byteBuffer.put("你好".getBytes());
                    byteBuffer.flip();
                    channel.write(byteBuffer);
                    selectionKey.interestOps(SelectionKey.OP_READ);
                    selector.wakeup();
                }
                selectionKeys.remove(selectionKey);
            }
        }
    }
}
