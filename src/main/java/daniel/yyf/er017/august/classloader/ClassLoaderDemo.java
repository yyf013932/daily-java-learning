package daniel.yyf.er017.august.classloader;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Method;

/**
 * JVM启动后不会将所有的类加载到内存里面，把类（.class文件）加载的内存里需要classloader来完成
 * 加载进入内存的Class如果要相同必须加载他们的ClassLoader也要相同。即，即使同一个类(.class文件完全相同)，不同的ClassLoader加载的是不相同的。
 * Java中有4种类加载器：
 * <p>
 * Bootstrap ClassLoader: 这个加载器负责在JVM启动时，将{JAVA_HOME}\lib里的类（如rt.jar)加载到内存里
 * 这个类是通过native代码进行实现，无法在Java代码中获得具体的类
 * <p>
 * Extension ClassLoader:负责加载{JAVA_HOME}\lib\ext下的类，或者通过系统变量java.ext.dirs指定的路径中的类库
 * <p>
 * Application ClassLoader:负责加载classpath下的类库，或者通过-cp指定路径的类。在没有用户自定义的类库时，默认使用此加载器
 * <p>
 * Customized ClassLoader:用户自定义加载器，用户自行定义加载方法。
 * <p>
 * 类的加载使用了一种叫 双亲委派模型 的方法，即除了Bootstrap ClassLoader以外的每个类加载器都有一个父加载器，在加载类时，先将加载任务
 * 交给父加载器，当父加载器无法找到类时才使用子加载器。
 * 这样做是为了保证类的加载有层次性，某些特定的类只能被特定的加载器加载。
 * 但是有一个问题是父加载器不可见子加载器加载的类，参见{}
 * <p>
 * -------------------------
 * | Bootstrap ClassLoader |
 * ------------------------
 * |
 * -------------------------
 * | Extension ClassLoader |
 * -------------------------
 * |
 * -------------------------
 * |Application ClassLoader|
 * -------------------------
 * |
 * -------------------------
 * |Customized ClassLoader |
 * -------------------------
 * <p>
 * <p>
 * <p>
 * Created by daniel.yyf on 2017/8/2.
 */
public class ClassLoaderDemo {

    /**
     * 打印一个类的classLoader层次信息
     *
     * @param clazz
     */
    static void showClassLoaderHierachy(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        System.out.println("class loaders for " + clazz.getName());
        while (classLoader != null) {
            System.out.println(classLoader.getClass().getName());
            classLoader = classLoader.getParent();
        }
        System.out.println("------------------------------------");
    }

    public static void main(String[] args) throws Exception {

        Class<?> cpClazz = A.class;
        showClassLoaderHierachy(cpClazz);

        //这里需要更改为自己文件目录下target/classes文件夹
        ClassLoader classLoader = new FileSystemClassLoader("D:\\git-repository\\daily-java-learning\\target\\classes");
        Class<?> clazz = classLoader.loadClass("daniel.yyf.er017.august.classloader.A");
        showClassLoaderHierachy(clazz);

        //使用不同加载器加载的同一份字节码文件的类也是不一样的
        //false
        System.out.println(A.class == clazz);

        //使用反射创建一个实例
        Object obj = clazz.newInstance();

        try {
            //这里无法强制转换
            A a = (A) obj;
        } catch (Exception e) {
            System.out.println("cast failed!");
        }

        //可以使用反射来调用
        Method method = clazz.getDeclaredMethod("say");
        //say hello
        method.invoke(obj);

    }
}


/**
 * 自定义的类加载器
 */
class FileSystemClassLoader extends ClassLoader {

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
