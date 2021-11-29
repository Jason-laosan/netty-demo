package com.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: laosan
 * Date: 2021/6/19
 * Time: 10:52 AM
 * Describe:
 */
public class NIOFileChannel3 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("./file03.txt");

        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("./file02.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) {

            //清空buffer
            byteBuffer.clear();

            int read = inputStreamChannel.read(byteBuffer);
            System.out.println("read=" + read);
            if (read == -1) {//read finished
                break;
            }
            //读写反转
            byteBuffer.flip();
            int write = outputStreamChannel.write(byteBuffer);
            if (write == -1) {
                break;
            }
        }
        //close stream
        fileInputStream.close();
        fileOutputStream.close();
    }
}
