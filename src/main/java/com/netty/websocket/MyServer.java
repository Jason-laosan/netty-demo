package com.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import java.time.LocalDateTime;

/**
 * @author: laosan
 * Date: 2021/6/29
 * Time: 10:44 PM
 * Describe:
 */
public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //因为是http协议,使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写， 添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                           1 http数据在传输过程中是分段的， 可以讲多个端聚合 
                           2 这就是为什么当浏览器发送大量数据是， 就会发送多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                            1 对应 webSocket 他的数据是以帧(frame)形式传递
                            2可以看到webSocketFrame 下面有六个子类
                            3 浏览器请求是ws://localhost:9999/hello 标识请求的uri
                            4 WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议， 保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            //自定义handler,处理业务逻辑
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(9999).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}

//标识一个文本帧(frame)
class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器接收到消息: " + msg.text());
        //回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间 " + LocalDateTime.now() + " " + msg.text()));
    }

    /**
     * 当web客户端链接后， 触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id 标识唯一的值， LongText 是唯一的， ShortText 不是唯一
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asShortText());
    }

    /**
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemove被滴啊用" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常繁盛 " + cause.getMessage());
        ctx.close();
    }
}
