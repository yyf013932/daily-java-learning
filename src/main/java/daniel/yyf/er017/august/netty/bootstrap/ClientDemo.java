package daniel.yyf.er017.august.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * Created by daniel.yyf on 2017/8/21.
 */
public class ClientDemo {
    Bootstrap bootstrap;
    EventLoopGroup workGroup;
    ChannelFuture channelFuture;
    private static int port = 12345;

    void start() {
        workGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpRequestEncoder());
                        ch.pipeline().addLast(new HttpResponseDecoder());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });

    }

    void sendMessage(String host, String msg) {
        try {
            ChannelFuture f = bootstrap.connect(host, port).sync();
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, new URI(host + ":" + 1234).toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF8")));
            f.channel().writeAndFlush(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        ClientDemo c = new ClientDemo();
        c.start();
        c.sendMessage("localhost", "hello");
    }
}

class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("in");
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();
        }
    }

}
