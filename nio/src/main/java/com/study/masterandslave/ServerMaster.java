package com.study.masterandslave;

import java.io.IOException;

public class ServerMaster {
    public static void main(String[] args) {
        try {
            TCPMasterReactor tcpReactor = new TCPMasterReactor(9087);
            new Thread(tcpReactor).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
