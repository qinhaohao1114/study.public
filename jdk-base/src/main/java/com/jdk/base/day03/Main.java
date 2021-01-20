package com.jdk.base.day03;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        MyCallable myCallable = new MyCallable();

        FutureTask<String> futureTask = new FutureTask<>(myCallable);
        // 启动线程，执行callable的业务
        new Thread(futureTask).start();

        // 同步等待callable的返回值
        String result = futureTask.get();
        System.out.println(result);
        System.out.println("main线程运行结束");
    }
}
