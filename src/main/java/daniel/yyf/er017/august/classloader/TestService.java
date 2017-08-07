package daniel.yyf.er017.august.classloader;

/**
 * Created by daniel.yyf on 2017/8/2.
 */
public class TestService {
    //不同加载器加载的是不同的，静态变量也不会共享
    private static int count = 0;

    public void say() {
        System.out.println("say hello");
    }

    public static int getAndIncrease() {
        return count++;
    }
}