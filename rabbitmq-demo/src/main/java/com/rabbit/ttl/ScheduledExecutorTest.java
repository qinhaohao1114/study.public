package com.rabbit.ttl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class ScheduledExecutorTest {

    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        //线程工程
        ThreadFactory factory = Executors.defaultThreadFactory();
        //使用线程池
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(10, factory);
        System.out.println("开始等待用户付款10秒： " + format.format(new Date()));

        service.schedule(() -> System.out.println("用户没有付款，交易取消:" + format.format(new Date())), 10, TimeUnit.SECONDS);

    }
}
