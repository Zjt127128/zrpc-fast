package com.zrpc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

//Netty 在网络传输中的最小单位，bytebuffer
public class NettyTest {
    //构造一个bytebuffer
    @Test
    public void testByteBuf(){
        //构建出一个bytebuffer
        ByteBuf buffer = Unpooled.buffer();
    }

    //通过逻辑组装实现零拷贝
    @Test
    public void testCompositeByteBuf(){
        ByteBuf header = Unpooled.buffer();
        ByteBuf body = Unpooled.buffer();
        //通过逻辑组装而不是拷贝实现在jvm中的零拷贝
        CompositeByteBuf byteBufs = Unpooled.compositeBuffer();
        byteBufs.addComponents(header,body);
    }

    //通过共享byte数组的内容实现零拷贝
    @Test
    public void testByteToByteBuffer(){
        byte[] bytes = new byte[1024];
        byte[] bytes1 = new byte[1024];
        //把byte中的数据合并到一个bytebuffer中，通过共享而不是拷贝
        ByteBuf buffer = Unpooled.wrappedBuffer(bytes,bytes1);
        //通过slice方法又可分割成多个bytes，也是没有拷贝操作，而是共享bytebuf存储空间的不同部分而已
        ByteBuf slice1 = buffer.slice(1, 10);
        ByteBuf slice2 = buffer.slice(11, 30);
    }
}
