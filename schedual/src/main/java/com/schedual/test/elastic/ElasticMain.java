package com.schedual.test.elastic;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

/**
 * @Author qinhaohao
 * @Date 2020/7/3 4:48 下午
 **/
public class ElasticMain {

    public static void main(String[] args) {
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration("127.0.0.1:2181","data-archive-job");
        CoordinatorRegistryCenter coordinatorRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
        coordinatorRegistryCenter.init();
        // 配置任务
        JobCoreConfiguration jobCoreConfiguration =
                JobCoreConfiguration.newBuilder("archive-job","*/2 * * * * ?",1).build();
                        SimpleJobConfiguration simpleJobConfiguration = new
                                SimpleJobConfiguration(jobCoreConfiguration,BackupJob.class.getName());
        // 启动任务
        new JobScheduler(coordinatorRegistryCenter,
                LiteJobConfiguration.newBuilder(simpleJobConfiguration).build()).init();
    }
}
