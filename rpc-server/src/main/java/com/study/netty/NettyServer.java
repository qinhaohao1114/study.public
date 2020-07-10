package com.study.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author qinhaohao
 * @Date 2020/7/9 2:07 下午
 **/
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        //1.创建 NioEventLoopGroup的两个实例:bossGroup workerGroup
        // 当前这两个实例代表两个线程池，默认线程数为CPU核心数乘2
        // bossGroup接收客户端传过来的请求
        // workerGroup处理请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        //2、创建服务启动辅助类:组装一些必要的组件
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //设置组,第一个bossGroup负责连接, workerGroup负责连接之后的io处理
        serverBootstrap.group(bossGroup,workGroup)
                //channel方法指定服务器监听的通道类型
                .channel(NioServerSocketChannel.class)
                //设置channel handler , 每一个客户端连接后,给定一个监听器进行处理
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //传输通道
                        ChannelPipeline pipeline = ch.pipeline();
                        //在通道上添加对通道的处理器 , 该处理器可能还是一个监听器
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        //监听器队列上添加我们自己的处理方式..
                        pipeline.addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                System.out.println(s);
                            }
                        });
                    }
                });
        //bind监听端口
        ChannelFuture f = serverBootstrap.bind(8000).sync();
        System.out.println("tcp server start success..");
        //会阻塞等待直到服务器的channel关闭
        f.channel().closeFuture().sync();
    }
}
