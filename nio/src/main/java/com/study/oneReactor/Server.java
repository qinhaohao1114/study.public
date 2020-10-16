package com.study.oneReactor;

import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        try {
            TCPReactor tcpReactor = new TCPReactor(9087);
            tcpReactor.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
