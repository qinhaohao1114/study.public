package com.study.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServerMain {
    public static void main(String[] args) throws IOException {
        start();
    }

    private static void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9089);
        while (true){
            final Socket socket = serverSocket.accept();
            new Thread(){
                public void run(){
                    try {
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();
                        byte[] bytes = new byte[1024];
                        while (in.read(bytes)!=-1){
                            out.write(bytes);
                            out.flush();
                        }
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
    }
}
