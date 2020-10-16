package com.study.masterandslave;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class MasterWorkStateMaster implements MasterHandlerState {
    @Override
    public void changeState(TCPMasterHandler h) {
        h.setState(new MasterWirteStateMaster());
    }

    @Override
    public void handle(TCPMasterHandler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {

        changeState(h);
    }
}
