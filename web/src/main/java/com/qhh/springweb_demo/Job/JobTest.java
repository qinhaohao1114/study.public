package com.qhh.springweb_demo.Job;

import com.qhh.springweb_demo.annotations.SelfJob;
import com.qhh.springweb_demo.annotations.SelfTask;
import org.springframework.stereotype.Component;

/**
 * @Author qinhaohao
 * @Date 2020/7/3 5:29 下午
 **/
@Component
@SelfJob
public class JobTest {


    @SelfTask(cron = "*/2 * * * * ?",taskName = "job1")
    public void testJob1(){
        System.out.println("job1任务开始");
    }

    @SelfTask(cron = "*/2 * * * * ?",taskName = "job2")
    public void testJob2(){
        System.out.println("job2任务开始");
    }
}
