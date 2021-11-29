package com.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: laosan
 * Date: 2021/6/19
 * Time: 10:16 AM
 * Describe:
 */
public class NIOFileChannel {
    public static void main(String[] args) throws Exception{
        String str = "hello, 老三";

        FileOutputStream fileOutputStream = new FileOutputStream("./file01.txt");

        FileChannel channel = fileOutputStream.getChannel();

        // 创建缓冲区 ByteBuffer
        ByteBuffer buffer= ByteBuffer.allocate(1024);
        buffer.put(str.getBytes());
        //读写翻转
        buffer.flip();
        channel.write(buffer);

        channel.close();


    }
}
