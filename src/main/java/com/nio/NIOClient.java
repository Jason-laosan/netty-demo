package com.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author: laosan
 * Date: 2021/6/19
 * Time: 9:04 PM
 * Describe:
 */
public class NIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间 客户端不会阻塞，可以继续做其他工作");
            }
        }

        String str = "hello laosan";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());

        socketChannel.write(byteBuffer);

        System.in.read();

    }
}
