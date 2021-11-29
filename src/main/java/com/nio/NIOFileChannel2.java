package com.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: laosan
 * Date: 2021/6/19
 * Time: 10:39 AM
 * Describe:
 */
public class NIOFileChannel2 {
    public static void main(String[] args) throws Exception{
        File file = new File("./file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer allocate = ByteBuffer.allocate((int) file.length());

        channel.read(allocate);
        System.out.println(new String(allocate.array()));
        channel.close();
    }
}
