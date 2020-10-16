package com.study.bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class BioClientMain {

    public static void main(String[] args) throws Exception {
        final Socket socket = new Socket("127.0.0.1",9089);
        new Thread(){
            @Override
            public void run(){
                while (true){
                    try {
                        byte[] bytes = new byte[1024];
                        while (socket.getInputStream().read(bytes) !=-1){
                            System.out.println(new String(bytes));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String s = scanner.nextLine();
                socket.getOutputStream().write(s.getBytes());
            }

        }
    }
}
