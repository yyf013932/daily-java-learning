package daniel.yyf.er017.july.spi;

/**
 * 在META-INF/services/danie.yyf.er017.july.spi.SPIService文件中填入danie.yyf.er017.july.spi.SPIServiceImpl
 *
 * Created by daniel.yyf on 2017/7/31.
 */
public class SPIServiceImpl implements SPIService {
    @Override
    public void print() {
        System.out.println("spi service");
    }
}
