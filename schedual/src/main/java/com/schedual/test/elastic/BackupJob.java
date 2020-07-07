package com.schedual.test.elastic;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @Author qinhaohao
 * @Date 2020/7/3 4:27 下午
 **/
public class BackupJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("执行elastic Job逻辑");
    }
}
