package com.schedual.test;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Author qinhaohao
 * @Date 2020/7/3 3:36 下午
 **/
public class DemoJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("我是一个定时任务逻辑");
    }
}
