package com.qhh.springweb_demo.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.qhh.springweb_demo.annotations.SelfJob;
import com.qhh.springweb_demo.annotations.SelfTask;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author qinhaohao
 * @Date 2020/7/3 4:51 下午
 **/
@Component
public class JobProcess implements BeanPostProcessor {

    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryCenter;


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!bean.getClass().isAnnotationPresent(SelfJob.class)){
            return bean;
        }
        Class<?> aClass = bean.getClass();
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(SelfTask.class)){
                continue;
            }
            SelfTask selfTask = method.getAnnotation(SelfTask.class);
            String cron = selfTask.cron();
            String taskName = selfTask.taskName();
            SimpleJob simpleJob = shardingContext -> {
                try {
                    method.invoke(bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            };
//            runJob(cron,taskName,simpleJob.getClass().getName());
        }
        return bean;
    }



    private void runJob(String cron,String jobName,String className){
        JobCoreConfiguration jobCoreConfiguration =
                JobCoreConfiguration.newBuilder(jobName,cron,1).build();
        SimpleJobConfiguration simpleJobConfiguration = new
                SimpleJobConfiguration(jobCoreConfiguration,className);
        // 启动任务
        new JobScheduler(coordinatorRegistryCenter,
                LiteJobConfiguration.newBuilder(simpleJobConfiguration).build()).init();
    }


}
