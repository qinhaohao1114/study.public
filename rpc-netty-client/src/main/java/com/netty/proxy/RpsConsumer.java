package com.netty.proxy;

import com.netty.handler.UserClientHandler;
import com.netty.pojo.SelfRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author qinhaohao
 * @Date 2020/7/10 11:03 上午
 **/
public class RpsConsumer {
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static UserClientHandler client;
    /**
     * 创建一个代理对象
     */
    public Object createProxy(final Class<?> serviceClass) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass}, (proxy, method, args) -> {
                    if (client == null) {
                        initClient();
                    } // 设置参数
                    SelfRequest selfRequest = new SelfRequest();
                    selfRequest.setClassName(serviceClass.getName());
                    selfRequest.setMethodName(method.getName());
                    LinkedHashMap<String, Object> params = new LinkedHashMap<>();
                    for (Object arg : args) {
                        String name = arg.getClass().getName();
                        params.put(name,arg);
                    }
                    selfRequest.setParams(params);
                    client.setResultType(method.getReturnType());
                    client.setPara(selfRequest);
                    return executor.submit(client).get();
                });
    }
    /**
     * 初始化客户端*/
    private static void initClient() {
        client = new UserClientHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new StringDecoder());
                        p.addLast(new StringEncoder());
                        p.addLast(client);
                    }
                });
        try {
            b.connect("127.0.0.1", 8990).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
