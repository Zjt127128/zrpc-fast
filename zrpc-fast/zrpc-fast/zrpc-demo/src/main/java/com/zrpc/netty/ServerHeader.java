package com.zrpc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class ServerHeader extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf input = (ByteBuf) msg;
        System.out.println("受到客服端发来的消息"+input.toString(StandardCharsets.UTF_8));
        //同时给客服端发送消息
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello,client".getBytes(StandardCharsets.UTF_8)));
    }
}
