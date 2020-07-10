package com.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NIOClient{

    public static void main(String[] args) {
        //创建远程地址
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8889);
        SocketChannel channel=null;
        //定义缓存
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            channel=SocketChannel.open();
            channel.connect(address);
            Scanner sc = new Scanner(System.in);
            while (true){
                System.out.println("客户端即将给服务器发送数据");
                String line = sc.nextLine();
                if (line.equals("exit")){
                    break;
                }
                buffer.put(line.getBytes("UTF-8"));
                buffer.flip();
                channel.write(buffer);
                buffer.clear();

                int readLen = channel.read(buffer);
                if (readLen==-1){
                    break;
                }
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                System.out.println("收到了服务器发送的数据："+new String(bytes,"UTF-8"));
                buffer.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null!=channel){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
