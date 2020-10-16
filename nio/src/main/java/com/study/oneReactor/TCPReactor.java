package com.study.oneReactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TCPReactor implements Runnable{

    private final ServerSocketChannel ssc;
    private final Selector selector;

    public TCPReactor(int port) throws IOException {
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(port));
            selector = Selector.open();
            ssc.configureBlocking(false);
            SelectionKey sk = ssc.register(selector, SelectionKey.OP_ACCEPT);
            sk.attach(new Acceptor(ssc,selector));
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            try {
                if (selector.select()==0){
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey next = iterator.next();
                    doDispatch(next);
                    iterator.remove();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }



    }

    private void doDispatch(SelectionKey sk) {
        Runnable attachment = (Runnable) sk.attachment();
        attachment.run();
    }
}
