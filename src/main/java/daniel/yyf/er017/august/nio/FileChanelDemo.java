package daniel.yyf.er017.august.nio;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by daniel.yyf on 2017/8/17.
 */
public class FileChanelDemo {
    static final String PREFIX = FileChanelDemo.class.getClassLoader().getResource(".").getPath() + "august/nio/";
    static final String INPUT_FILE = PREFIX + "inputFile.txt";
    static final String OUTPUT_FILE = PREFIX + "outputFile.txt";
    static final String OUTOUT_FILE_CHANEL = PREFIX + "outputFileChanel.txt";
    static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        readAndPrint();
        readAndWrite();
        chanelTransform();
    }

    static void readAndPrint() throws Exception {
        try (FileInputStream inputStream = new FileInputStream(INPUT_FILE)) {
            FileChannel fc = inputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (fc.read(buffer) > 0) {
                System.out.print(new String(buffer.array()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void readAndWrite() throws Exception {
        FileChannel ifc = new FileInputStream(INPUT_FILE).getChannel();
        FileChannel ofc = new FileOutputStream(OUTPUT_FILE).getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        while (ifc.read(byteBuffer) > 0) {
            byteBuffer.flip();
            ofc.write(byteBuffer);
        }
        ifc.close();
        ofc.close();
    }

    static void chanelTransform() throws Exception {
        FileChannel ifc = new FileInputStream(INPUT_FILE).getChannel();
        FileChannel ofc = new FileOutputStream(OUTOUT_FILE_CHANEL).getChannel();
        ifc.transferTo(0, ifc.size(), ofc);
    }
}
