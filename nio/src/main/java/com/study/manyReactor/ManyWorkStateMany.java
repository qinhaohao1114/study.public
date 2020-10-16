package com.study.manyReactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class ManyWorkStateMany implements ManyHandlerState {
    @Override
    public void changeState(TCPManyHandler h) {
        h.setState(new ManyWirteStateMany());
    }

    @Override
    public void handle(TCPManyHandler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {

        changeState(h);
    }
}
