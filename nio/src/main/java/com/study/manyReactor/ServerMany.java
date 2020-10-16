package com.study.manyReactor;

import com.study.oneReactor.TCPReactor;

import java.io.IOException;

public class ServerMany {
    public static void main(String[] args) {
        try {
            TCPManyReactor tcpReactor = new TCPManyReactor(9087);
            tcpReactor.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
