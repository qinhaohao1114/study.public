package com.study.io;

import java.net.Socket;

/**
 * @Author qinhaohao
 * @Date 2020/7/9 11:08 上午
 **/
public class IOClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8081);
        socket.getOutputStream().write("hello".getBytes());
        socket.getOutputStream().flush();
        System.out.println("server send back data ======");
        byte[] bytes = new byte[1024];
        int len = socket.getInputStream().read(bytes);
        System.out.println(new String(bytes,0,len));
        socket.close();
    }
}
