package daniel.yyf.er017.august.netty.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

/**
 * Created by daniel.yyf on 2017/8/21.
 */
public class ServerDemo {
    ServerBootstrap serverBootstrap;
    EventLoopGroup bossLoop;
    EventLoopGroup workLoop;

    ChannelFuture future;

    void start() {
        serverBootstrap = new ServerBootstrap();
        bossLoop = new NioEventLoopGroup();
        workLoop = new NioEventLoopGroup();

        serverBootstrap
                .group(bossLoop, workLoop)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpResponseEncoder());
                        ch.pipeline().addLast(new HttpRequestDecoder());
                        ch.pipeline().addLast(new ServerHandlerDemo());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            future = serverBootstrap.bind(12345).sync();
        } catch (InterruptedException e) {
            System.err.println("init error");
            e.printStackTrace();
        }
    }

    void close() {
        try {
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workLoop.shutdownGracefully();
            bossLoop.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        ServerDemo serverDemo = new ServerDemo();
        serverDemo.start();
    }
}

class ServerHandlerDemo extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            System.out.println("uri" + request.uri());
        }
        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            String content = httpContent.content().toString(StandardCharsets.UTF_8);
            System.out.println("get respond" + content);
            ChannelFuture future = ctx.writeAndFlush("get your request");
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
