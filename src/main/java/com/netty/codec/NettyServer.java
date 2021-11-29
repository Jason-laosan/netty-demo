package com.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * @author: laosan
 * Date: 2021/6/21
 * Time: 9:26 AM
 * Describe:
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //boss只做连接请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //真正和客户端业务处理,交给workGroup完成
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {

            //创建服务器端对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建通道对象
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder", new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });//workerGroup的EventLoop对应的管道设置处理器
            System.out.println("......服务器 准备好了.....");

            //绑定端口并且同步
            ChannelFuture cf = bootstrap.bind(9999).sync();
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("监听端口9999成功");
                    } else {
                        System.out.println("监听端口9999失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();

        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
