package com.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: laosan
 * Date: 2021/6/19
 * Time: 7:00 PM
 * Describe:
 */
public class NIOServer {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        serverSocketChannel.configureBlocking(false);

        //serverSocketChannel注册到selector  事件 OP_ACCEPT

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待1秒， 无连接");
                continue;
            }

            //如果返回>0 获取到相关的 selectionKes集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                //获取selectionKey
                SelectionKey key = iterator.next();
                //根据key对应的channel 事件做响应的处理
                if (key.isAcceptable()) {
                    //给该客户端生成一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端链接成功，生成了一个socketChannel" + socketChannel.hashCode());
                    //将socketChannel注册到selector上
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                } else if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    socketChannel.read(buffer);
                    System.out.println("from 客户端 " + new String(buffer.array()));
                }
                //手动从集合中移除当期那的selectionKey, 防止重复操作
                iterator.remove();

            }

        }

    }
}
