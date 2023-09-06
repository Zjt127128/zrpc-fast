package com.zrpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Client implements Serializable {
    public void run(){
        //定义干活的线程池，I/O线程池
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        //客户端辅助启动类
        Bootstrap bootstrap = new Bootstrap();

        //启动一个客服端
        try {
            bootstrap.group(eventExecutors)
                    //实例化一个channl
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(8080))
                    //进行通道初始化配置
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ClientHeader());
                        }
                    });
            //连接到远程节点；等待连接完成
            ChannelFuture channelFuture = bootstrap.connect().sync();
            //发送消息到服务端
            channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer("hello,netty".getBytes(StandardCharsets.UTF_8)));
            //阻塞操作，closeFuture()开启了一个channel的监听器（这期间channel在进行各项工作），直到链路断开
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                eventExecutors.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void main(String[] args) {
        new Client().run();
    }
}
