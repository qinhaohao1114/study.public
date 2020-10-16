package com.study.manyReactor;

import com.luban.manyReactor.TCPHandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public interface ManyHandlerState {


    void changeState(TCPManyHandler h);

    void handle(TCPManyHandler h, SelectionKey sk, SocketChannel sc,
                ThreadPoolExecutor pool) throws IOException;
}
