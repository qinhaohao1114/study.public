package com.study.masterandslave;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class TCPMasterSubReactor implements Runnable {
    private final Selector selector;
    private boolean restart = false;
    int num;

    public TCPMasterSubReactor(Selector selector, int num) {
        this.selector = selector;
        this.num = num;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                System.out.println("waiting for restart");
                while (!Thread.interrupted()&&!restart){
                    if (selector.select()==0){
                        continue;
                    }
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()){
                        dispatch(it.next());
                        it.remove();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable) (key.attachment()); // 根據事件之key綁定的對象開新線程
        if (r != null)
            r.run();
    }
    public void setRestart(boolean restart) {
        this.restart = restart;
    }
}
