package com.zrpc;

import com.zrpc.netty.Client;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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

    @Test
    public void message() throws IOException {
        ByteBuf message = Unpooled.buffer();
        //魔数，用户识别该协议。
        message.writeBytes("zjt".getBytes(StandardCharsets.UTF_8));
        //协议版本号
        message.writeByte(1);
        //消息类型
        message.writeByte(0x01);
        //序列号类型
        message.writeByte(1);
        //请求id
        message.writeLong(15434556L);
        //head长度
        message.writeShort(124);
        //用对象流转换为字节数组
        Client client = new Client();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(client);
        byte[] bytes = outputStream.toByteArray();
        message.writeBytes(bytes);

        printAsBinary(message);
    }

    public static void printAsBinary(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(byteBuf.readerIndex(), bytes);

        String binaryString = ByteBufUtil.hexDump(bytes);
        StringBuilder formattedBinary = new StringBuilder();

        for (int i = 0; i < binaryString.length(); i += 2) {
            formattedBinary.append(binaryString.substring(i, i + 2)).append(" ");
        }

        System.out.println("Binary representation: " + formattedBinary.toString());
    }

    @Test
    public void GzipCompress() throws IOException {
        //压缩前的字节码
        byte[] bytes = {11, 12, 13, 14, 11, 11, 11, 11, 11, 11, 15, 16, 88, 17, 14, 58,11, 12, 13, 14, 11, 11, 11, 11, 11, 11, 15, 16, 88, 17, 14, 58,12, 13, 14, 11, 11, 11, 11, 11, 11, 15, 16, 88, 17, 14, 58,12, 13, 14, 11, 11, 11, 11, 11, 11, 15, 16, 88, 17, 14, 58,12, 13, 14, 11, 11, 11, 11, 11, 11, 15, 16, 88, 17, 14, 58,12, 13, 14, 11, 11, 11, 11, 11, 11, 15, 16, 88, 17, 14, 58,12, 13, 14, 11, 11, 11, 11, 11, 11, 15, 16, 88, 17, 14, 58,12, 13, 14, 11, 11, 11, 11, 11, 11, 15, 16, 88, 17, 14, 58,12, 13, 14, 11, 11, 11, 11, 11, 11, 15, 16, 88, 17, 14, 58,};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        gzipOutputStream.write(bytes);
        gzipOutputStream.flush();
        gzipOutputStream.finish();
        byte[] bytes1 = outputStream.toByteArray();
        System.out.println("压缩前的长度------>"+bytes.length);
        System.out.println("压缩后的长度------>"+bytes1.length);

        //解压缩
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes1);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        byte[] bytes3 = new byte[1024];
        int n;
        while (( n = gzipInputStream.read(bytes3)) > -1){
            outputStream1.write(bytes3,0,n);
        }
        byte[] bytes2 = outputStream1.toByteArray();
        System.out.println("解压缩后的长度为------>"+bytes2.length);
    }
}
