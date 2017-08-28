package daniel.yyf.er017.august.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by daniel.yyf on 2017/8/17.
 */
public class SocketChanelDemo {
    public static void main(String[] args) throws Exception {
        TCPClient tcpClient = new TCPClient("127.0.0.1", 12345);
        tcpClient.connect();
        for (int i = 0; i < 5; i++) {
            tcpClient.sendMessage("hello! this is " + i + "\n");
            TimeUnit.MILLISECONDS.sleep(200);
        }
        TimeUnit.SECONDS.sleep(2);
        tcpClient.close();

    }
}

class TCPClient {
    SocketChannel socketChannel;
    private String host;
    private int port;
    Selector selector;
    Thread readThread;

    public TCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(host, port));
        socketChannel.configureBlocking(false);

        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

        readThread = new Thread(() -> {
            while (true) {
                try {
                    if (selector.select(3000) == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
                    while (keyIter.hasNext()) {
                        SelectionKey key = keyIter.next();
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        if (key.isReadable()) {
                            ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                            byteBuffer.clear();
                            if (socketChannel.read(byteBuffer) == -1)
                                socketChannel.close();
                            System.out.println("received message :" + new String(byteBuffer.array()));

                        }
                        keyIter.remove();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        readThread.start();
    }

    public void sendMessage(String msg) throws IOException {
        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
    }

    public void close() throws IOException {
        socketChannel.close();
        selector.close();
    }


}
