package daniel.yyf.er017.august.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by daniel.yyf on 2017/8/17.
 */
public class SocketServerDemo {


    public static void main(String[] args) {
        TCPServer tcp = new TCPServer();
        try {
            tcp.init();
            tcp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class TCPServer {
    static final int BUFF_SIZE = 1024;
    static final int PORT = 12345;
    static final int TIME_OUT = 3000;
    static final int THREAD_POOL = 5;
    Selector selector;
    ServerSocketChannel serverSocketChannel;
    ExecutorService threadPool;

    void init() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        //设置为非阻塞，非阻塞才能使用selector
        serverSocketChannel.configureBlocking(false);
        //注册selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        threadPool = Executors.newFixedThreadPool(THREAD_POOL);
    }

    void start() {
        Runnable r = () -> {
            while (true) {
                try {
                    if (selector.select(TIME_OUT) == 0) {
                        System.out.println("wait...");
                        continue;
                    }
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    SocketChannel client;
                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        if (key.isAcceptable()) {
                            client = ((ServerSocketChannel) key.channel()).accept();
                            System.out.println("remoting connection from :" + client.getRemoteAddress());
                            client.configureBlocking(false);
                            client.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(BUFF_SIZE));
                        } else if (key.isConnectable()) {
                            System.out.println("connect");
                        } else if (key.isReadable()) {
                            client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            buffer.clear();
                            if (client.read(buffer) == -1) {
                                client.close();
                            } else {
                                buffer.flip();
                                String msg = Charset.forName("UTF-8").decode(buffer).toString();
                                System.out.println("receive message:" + msg + ",from :" + client.getRemoteAddress());
                                client = (SocketChannel) key.channel();
                                String sendString = "Hello,Client.Received your message:" + msg + "\n";
                                client.write(ByteBuffer.wrap(sendString.getBytes()));
                            }
                        } else if (key.isWritable() && key.isValid()) {
                            System.out.println("write");
                        }
                        keys.remove();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        threadPool.submit(r);
    }

}
