package com.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: laosan
 * Date: 2021/6/19
 * Time: 1:05 PM
 * Describe:
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        //mappedByteBuffer 可以让文件直接在内存(堆外内存)中修改， 操作系统不需要拷贝一次

        RandomAccessFile rw = new RandomAccessFile("./file01.txt", "rw");
        FileChannel channel = rw.getChannel();
        /**
         *
         *param1  FileChannel.MapMode.READ_WRITE 读写模式
         *param 2 0 可以直接修改的起始位置
         *param3 5 映射到内存的大小(不是索引位置)
         * 实际类型 DirectByteBuffer
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0, (byte) 'H');
        map.put(3, (byte) '9');
        rw.close();
        System.out.println("修改成功");
    }
}
