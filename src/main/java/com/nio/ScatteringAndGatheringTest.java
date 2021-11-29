package com.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author: laosan
 * Date: 2021/6/19
 * Time: 1:17 PM
 * Describe:
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {
        /**
         * Scattering 将数据写入到buffer 可以采用buffer数组 依次写入 [分散]
         * Gathering 从buffer读取数据是， 可以用buffer数组 依次读
         */
        ServerSocketChannel open = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        open.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel accept = open.accept();

        int msgLength = 8;
        while (true) {
            int byteRead = 0;
            while (byteRead < msgLength) {
                long read = accept.read(byteBuffers);
                byteRead += read;
                System.out.println("byteRead=" + byteRead);

                Arrays.asList(byteBuffers).stream().map(buffer -> "position" + buffer.position() + " , limit =" + buffer.limit())
                        .forEach(System.out::println);
            }
            //将素有的buffer 进行flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            //将数据读取显示到客户端
            long byteWrite = 0;
            while (byteWrite < msgLength) {
                long write = accept.write(byteBuffers);
                byteWrite += write;
            }
            //将所有的buffer 进行clear
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());

            System.out.println("byteRead := " + byteRead + " byteWrite:= " + byteWrite + "  byteLength=" + msgLength);
        }


    }
}
