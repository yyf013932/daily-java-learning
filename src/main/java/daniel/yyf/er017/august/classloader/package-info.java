/**
 * 自定义ClassLoader的作用主要是为了动态加载资源和实现类的隔离。
 * 动态加载资源是指在运行时从指定的地方加载类文件
 * 隔离通常是在web容器中，为了隔离不同的项目使用的不同的jar包，因为同一个容器里的不同web项目可能会使用同一个jar的不同版本，
 * 为了解决这样的类库冲突，可以使用不同的加载器来进行隔离（因为类相同需要加载类的加载器也相同）
 *
 * ClassLoader通常使用双亲委派模型，但是这样会造成父加载器对子加载器加载的类不可见，在某些应用场景会成为障碍。
 * 如SPI，通常是在Bootstrap ClassLoader层次定义接口，而在Application 或更低的层次加载具体的实现类，需要一定
 * 的机制来打打破这样的双亲委派模型。
 * 主要有3种方式：
 * {@link sun.reflect.Reflection#getCallerClass()}可以获得调用者的类然后获得其加载器
 * {@link sun.misc.VM#latestUserDefinedLoader()}获得调用栈里第一个不为空的加载器
 * {@link java.lang.Thread#getContextClassLoader()}获得当前线程上下文加载器
 *
 * classloader 相关资料
 * http://www.javaworld.com/article/2077344/core-java/find-a-way-out-of-the-classloader-maze.html
 * http://blog.csdn.net/yangcheng33/article/details/52631940
 * Created by daniel.yyf on 2017/8/2.
 */
package daniel.yyf.er017.august.classloader;