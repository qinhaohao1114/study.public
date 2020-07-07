package com.schedual.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @Author qinhaohao
 * @Date 2020/7/3 3:30 下午
 **/
public class QuartzMain {

    public static Scheduler createScheduler() throws SchedulerException {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        return scheduler;
    }

    public static JobDetail createJob(){
        JobBuilder jobBuilder=JobBuilder.newJob(DemoJob.class);
        jobBuilder.withIdentity("jobName","myJob");
        JobDetail jobDetail = jobBuilder.build();
        return jobDetail;
    }

    public static Trigger createTrigger(){
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("triggerName", "myTrigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
                .build();

        return trigger;
    }

    public static void main(String[] args) throws SchedulerException {
        Scheduler scheduler = QuartzMain.createScheduler();

        JobDetail job = QuartzMain.createJob();

        Trigger trigger = QuartzMain.createTrigger();

        scheduler.scheduleJob(job,trigger);

        scheduler.start();
    }
}
