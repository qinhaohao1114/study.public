package com.jdk.base.day04;

public class MyQueue2 extends MyQueue {

    private String[] data = new String[10];
    // 下一条要存储记录的下标
    private int putIndex = 0;
    // 下一条要获取的元素下标
    private int getIndex = 0;
    // data中的元素个数
    private int size = 0;


    @Override
    public synchronized void put(String element) {
        if (size == data.length) {
            try {
                // 阻塞，等待
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 利用迭代，进行第二次抢对象锁
            put(element);
        } else {
            put0(element);
        }

    }

    private void put0(String element) {
        data[putIndex] = element;
        // 唤醒等待的消费者线程
        notify();
        ++size;
        ++putIndex;
        if (putIndex == data.length) putIndex = 0;
    }

    @Override
    public synchronized String get() {
        if (size == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 利用迭代，重新获取共享锁
            return get();
        } else {
            return get0();
        }
    }

    private String get0() {
        String result = data[getIndex];
        ++getIndex;
        if (getIndex == data.length) getIndex = 0;
        --size;
        // 唤醒生产者生产。因为对象锁是当前对象，this
        notify();
        return result;
    }

}
