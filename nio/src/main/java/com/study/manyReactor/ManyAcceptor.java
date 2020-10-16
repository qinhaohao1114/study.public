package com.study.manyReactor;

import com.study.oneReactor.TCPHandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ManyAcceptor implements Runnable{

    private final ServerSocketChannel ssc;

    private final Selector selector;

    public ManyAcceptor(ServerSocketChannel ssc, Selector selector) {
        this.ssc = ssc;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            SocketChannel sc = ssc.accept();
            System.out.println(sc.socket().getRemoteSocketAddress().toString() + " is connected.");
            if (sc!=null){
                sc.configureBlocking(false);
                SelectionKey sk = sc.register(selector, SelectionKey.OP_READ);
                selector.wakeup();
                sk.attach(new TCPManyHandler(sk,sc));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
