package com.inAndoutHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author: laosan
 * Date: 2021/7/3
 * Time: 10:11 PM
 * Describe:
 */
public class MyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(boss)
                    .channel(NioSocketChannel.class)
                    .handler(new MyClientInitializer());
            ChannelFuture sync = bootstrap.connect("127.0.0.1", 9999).sync();
            sync.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
        }
    }
}

class MyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyLongToByteEncoder());
        pipeline.addLast(new MyByteToLangDecoder2());
        pipeline.addLast(new MyClientHandler());
    }
}

class MyLongToByteEncoder extends MessageToByteEncoder<Long> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("MyLongToByteEncoder encode 被调用");
        System.out.println("msg= " + msg);
        out.writeLong(msg);
    }
}
class MyClientHandler extends SimpleChannelInboundHandler<Long>{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler  发送数据");
        ctx.writeAndFlush(Unpooled.copiedBuffer("lsjdflkjskdlfjlsjdflds", CharsetUtil.UTF_8));
//        ctx.writeAndFlush(123456L);
    }
}
