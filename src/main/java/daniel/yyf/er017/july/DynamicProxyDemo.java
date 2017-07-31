package daniel.yyf.er017.july;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK的动态代理主要需要实现的接口为{@link InvocationHandler},通过{@link Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)}类进行代理的创建
 * 代理类不是一个特定的类型，而是运行时在JVM中动态生成的
 * 动态代理会根据传入的接口参数，在最终的代理类中生成对应方法的的域（即类型为Method的Field）
 * 然后在调用时将这个作为{@link InvocationHandler#invoke(Object, Method, Object[])}的Method参数传入，进行实际的调用
 * 其中会生成3个固定的方法：toString()、equals()、hashCode(),域的名字依次为m2,m1,m0，可以根据这种方式自定义这3个方法（参见以下demo）
 * <p>
 * Created by daniel.yyf on 2017/7/31.
 */
public class DynamicProxyDemo {

    public static void main(String[] args) {

        Service serviceImpl = new ServiceImpl();
        ServiceProxy serviceProxy = new ServiceProxy(serviceImpl, serviceImpl.getClass().getInterfaces());
        Service service = (Service) serviceProxy.getInstance();
        service.sayHello("daniel");

        System.out.println("proxy toString:" + service.toString());
        System.out.println("proxy equals:" + service.equals(serviceProxy));
        System.out.println("proxy hashCode:" + service.hashCode());

    }

    interface Service {
        void sayHello(String name);
    }


}

class ServiceProxy implements InvocationHandler {
    DynamicProxyDemo.Service realService;
    Method hashCode, equal, toString;
    Object instance;

    public Object getInstance() {
        return instance;
    }

    public ServiceProxy(DynamicProxyDemo.Service realService, final Class<?>[] intfs) {
        this.realService = realService;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        instance = Proxy.newProxyInstance(classLoader, intfs, this);

        //JDK动态代理会为最终的代理类生成3个私有域，m0,m1,m2，分别为hashCode,equals,toString方法
        //在这里获得这3个方法的引用，用于在InvocationHandler中自定义实现
        try {
            Field hashCodeField = instance.getClass().getDeclaredField("m0");
            hashCodeField.setAccessible(true);
            hashCode = (Method) hashCodeField.get(instance);

            Field equalField = instance.getClass().getDeclaredField("m1");
            equalField.setAccessible(true);
            equal = (Method) equalField.get(instance);

            Field toStringField = instance.getClass().getDeclaredField("m2");
            toStringField.setAccessible(true);
            toString = (Method) toStringField.get(instance);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param proxy  代表最终生成的代理类
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before invocation");

        if (method == toString) {
            return "this is toString";
        }

        if (method == equal) {
            return proxy == args[0];
        }

        if (method == hashCode) {
            return System.identityHashCode(proxy);
        }

        return method.invoke(realService, args);
    }
}

class ServiceImpl implements DynamicProxyDemo.Service {
    @Override
    public void sayHello(String name) {
        System.out.println("hello " + name);
    }
}

