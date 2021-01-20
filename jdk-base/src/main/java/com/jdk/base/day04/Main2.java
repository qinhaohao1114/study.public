package com.jdk.base.day04;

public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        MyQueue2 myQueue = new MyQueue2();
        for (int i = 0; i < 5; i++) {
            new ConsumerThread(myQueue).start();
            new ProducerThread(myQueue).start();
        }

        Thread.sleep(10000);
        // 进程结束
        System.exit(0);
    }
}
