package com.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import java.util.concurrent.TimeUnit;

/**
 * @author: laosan
 * Date: 2021/6/21
 * Time: 9:40 AM
 * Describe:
 * 1 自定义一个handler 需要继承netty的某个handlerAdapter
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据事件(可以读取客户端发送的消息)
     *
     * @param ctx 上下文对象 含有管道pipeline,通道channel，地址
     * @param msg 客户端发送的数据， 默认是Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("server ctx = " + ctx);

        ByteBuf buf = (ByteBuf) msg;

        System.out.println("客户端发送消息是:  " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端的地址是: " + ctx.channel().remoteAddress());

        //方案1 有非常耗时的任务->异步执行 ->提交到channel对应的NIOEventLoop的taskQueue中
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(10 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端 喵 2", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.out.println("发生异常");
            }
        });
        //方案2 用户自定义定热任务-> 提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端 喵 2", CharsetUtil.UTF_8));
                    System.out.println("channel code = " + ctx.channel().hashCode());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);
    }

    /**
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===========================");
        //将数据写入缓冲区并刷新
        //发送数据前先编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello: 客户端~ 喵  ", CharsetUtil.UTF_8));
    }

    /**
     * 异常关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
