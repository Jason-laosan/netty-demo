package com.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author: laosan
 * Date: 2021/6/19
 * Time: 10:50 PM
 * Describe:
 */
public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                int count = selector.select(2000);
                // 有事件要处理
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress() + "上线");
                        }
                        //channel is read Event,
                        if (key.isReadable()) {
                            //处理读取事件
                            readData(key);

                        }
                        // 当前key的删除，防止重复处理
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待.............");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void readData(SelectionKey key) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            if (read > 0) {
                String msg = new String(buffer.array());
                System.out.println("from 客户端: " + msg);

                // 向其他客户端转发消息
                sendInfoToOtherClient(msg, channel);
            }

        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了.....");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // 转发消息给其他客户端(通道)
    public void sendInfoToOtherClient(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中.....");
        //遍历所有注册到select中的socketChannel 并排除自己
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }
}
