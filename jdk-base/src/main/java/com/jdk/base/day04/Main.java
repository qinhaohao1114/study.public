package com.jdk.base.day04;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MyQueue myQueue = new MyQueue();
        new ConsumerThread(myQueue).start();
        new ProducerThread(myQueue).start();

        Thread.sleep(10000);
        // 进程结束
        System.exit(0);
    }
}
