package com.netty.service;

import com.netty.handler.UserServerHandler;
import com.netty.pojo.User;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author qinhaohao
 * @Date 2020/7/10 10:21 上午
 **/
public class UserServiceImpl implements UserService{
    @Override
    public String sayHello(String word) {
        System.out.println("收到信息："+word);
        return "收到信息："+word;
    }

    @Override
    public User findUser(User user) {
        System.out.println("收到信息："+user.toString());
        return user;
    }


    public static void startServer(String hostName,int port){
      try {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(eventLoopGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel sh) throws Exception {
                        ChannelPipeline pipeline = sh.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new UserServerHandler());
                    }
                });

            bootstrap.bind(hostName,port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
