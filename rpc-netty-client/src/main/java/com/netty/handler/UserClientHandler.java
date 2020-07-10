package com.netty.handler;

import com.alibaba.fastjson.JSON;
import com.netty.pojo.SelfRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class UserClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;
    private String result;
    private SelfRequest para;
    private Class resultType;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        context = ctx;
    }

    /**
     * 收到服务端数据，唤醒等待线程
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {
        result = msg.toString();
        notify();
    }

    /**
     * 写出数据，开始等待唤醒
     */
    @Override
    public synchronized Object call() throws InterruptedException {
        String pa = JSON.toJSONString(this.para);
//        System.out.println("发送参数为:"+pa);
        context.writeAndFlush(pa);
        wait();
        return JSON.parseObject(result, resultType);
    }

    public void setPara(SelfRequest para) {
        this.para = para;
    }

    public void setResultType(Class clazz){
        this.resultType=clazz;
    }
}
