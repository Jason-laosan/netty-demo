package com.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: laosan
 * Date: 2021/6/18
 * Time: 10:04 PM
 * Describe:
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        //create threadPool
        // if client connect, create a thread to receive

        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while (true) {
            System.out.println("读取信息");
            final Socket socket = serverSocket.accept();
            System.out.println("链接到一个客户端");
            executorService.execute(() -> {
                handler(socket);
            });
            System.out.println("读取信息===========================");
        }
    }

    public static void handler(Socket socket) {
        try {
            System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端发送的数据
            while (true) {
                System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
                int read = inputStream.read(bytes);
                if (read != -1) {
                    //输出客户端发送的数据
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("关闭和client的链接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
