package com.study.masterandslave;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class MasterAcceptor implements Runnable {

    private final ServerSocketChannel ssc;
    private final int cores = Runtime.getRuntime().availableProcessors(); // 取得CPU核心數
    private final Selector[] selectors = new Selector[cores]; // 創建核心數個selector給subReactor用
    private TCPMasterSubReactor[] reactors = new TCPMasterSubReactor[cores];
    private Thread[] t = new Thread[cores]; // subReactor線程
    private int selIdx = 0;
    public MasterAcceptor(ServerSocketChannel ssc) {
        this.ssc = ssc;
        try {
            for (int i = 0; i < cores; i++) {
                selectors[i] = Selector.open();
                reactors[i]=new TCPMasterSubReactor(selectors[i],i);
                t[i]=new Thread(reactors[i]);
                t[i].start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            SocketChannel sc = ssc.accept();
            System.out.println(sc.socket().getRemoteSocketAddress().toString()
                    + " is connected.");
            if (sc!=null){
                sc.configureBlocking(false);
                reactors[selIdx].setRestart(true);
                selectors[selIdx].wakeup();
                SelectionKey sk = sc.register(selectors[selIdx], SelectionKey.OP_READ);
                selectors[selIdx].wakeup();
                reactors[selIdx].setRestart(false);
                sk.attach(new TCPMasterHandler(sk,sc));
                if (++selIdx == selectors.length)
                    selIdx = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
