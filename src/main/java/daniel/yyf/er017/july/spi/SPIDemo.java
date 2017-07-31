package daniel.yyf.er017.july.spi;


import java.util.ServiceLoader;

/**
 * SPI(service provider interface)类似于一种服务发现机制，很好的实现面向接口编程
 * 主要目的是为了不在代码中插入实现类的装配代码（类似于IOC），而是在META-INF/services目录下提供相关的实现类信息
 * META-INF/services/目录下的文件必须要和接口名相同
 * <p>
 * Created by daniel.yyf on 2017/7/31.
 */
public class SPIDemo {

    public static void main(String[] args) {
        //直接通过接口获取实现的服务
        ServiceLoader<SPIService> spiService = ServiceLoader.load(SPIService.class);

        for (SPIService spiService1 : spiService) {
            spiService1.print();
        }
    }

}


