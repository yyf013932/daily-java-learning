package daniel.yyf.er017.august.classloader;

/**
 * Created by daniel.yyf on 2017/8/3.
 */

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 自定义的类加载器
 */
public class FileSystemClassLoader extends ClassLoader {

    String rootPath;

    public FileSystemClassLoader(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * 这里覆盖这个方法的原因是为了更改加载类的流程，不从父加载器加载，直接从指定路径加载，为了方便测试
     * 实际使用中不建议这么做
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            Class<?> clazz = findClass(name);
            if (clazz != null)
                return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return super.loadClass(name);
    }

    /**
     * 自定义ClassLoader时建议覆盖这个方法
     * 读取class文件的字节码byte数组，然后通过defineClass创建对应的Class
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        String[] paths = name.trim().split("\\.");
        String filePath = StringUtils.join(paths, File.separator);
        try {
            byte[] buff = new byte[1024 * 4];
            int len = -1;
            FileInputStream fileInputStream = new FileInputStream(new File(rootPath + File.separator + filePath + ".class"));
            len = fileInputStream.read(buff);
            Class<?> clazz = null;
            clazz = defineClass(name, buff, 0, len);
            return clazz;
        } catch (IOException e) {

        }
        return null;
    }
}
