package com.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author: laosan
 * Date: 2021/6/19
 * Time: 11:05 AM
 * Describe:
 */
public class NIOFileChannel4 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("./1.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("./2.jpg");

        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        //使用 transferForm 完成文件拷贝

        outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size());

        inputStreamChannel.close();
        outputStreamChannel.close();

        fileInputStream.close();
        fileOutputStream.close();

    }
}
