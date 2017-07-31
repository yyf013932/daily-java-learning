package daniel.yyf.er017.july;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/**
 * 监听文件变化
 * @see FileAlterationMonitor 用来注册所有的Observer，提供运行线程
 * @see FileAlterationObserver 代表对一个根目录的监控
 * @see FileAlterationListener 对目录的监听器，监听目录和文件的增删改
 * Created by daniel.yyf on 2017/7/31.
 */
public class FileObserverDemo {

    public static void main(String[] args) throws Exception{
        FileMonitor fileMonitor = new FileMonitor();
        fileMonitor.monitor("D:/workspace");
        fileMonitor.start();
    }

}

class FileMonitor implements FileAlterationListener {
    FileAlterationMonitor monitor;

    public FileMonitor() {
        this(2000L);
    }

    public FileMonitor(long interval) {
        monitor = new FileAlterationMonitor(interval);
    }

    public void monitor(String filePath) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(filePath));
        monitor.addObserver(observer);
        observer.addListener(this);
    }

    public void stop() throws Exception {
        monitor.stop();
    }

    public void start() throws Exception {
        monitor.start();
    }


    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {
        System.out.println("start to monitor:" + fileAlterationObserver.getDirectory());
    }

    @Override
    public void onDirectoryCreate(File file) {
        System.out.println("directory created:" + file);
    }

    @Override
    public void onDirectoryChange(File file) {
        System.out.println("directory changed:" + file);
    }

    @Override
    public void onDirectoryDelete(File file) {
        System.out.println("directory deleted:" + file);
    }

    @Override
    public void onFileCreate(File file) {
        System.out.println("file created:" + file);
    }

    @Override
    public void onFileChange(File file) {
        System.out.println("file changed:" + file);
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("file deleted:" + file);
    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {
        System.out.println("stop monitor:" + fileAlterationObserver.getDirectory());
    }
}
