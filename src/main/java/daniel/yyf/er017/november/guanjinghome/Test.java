package daniel.yyf.er017.november.guanjinghome;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Test {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        String file = "/data/p03.cri";
        Instance inst = new Instance(file);
        inst.m = 4;
        inst.Q = 50;
        inst.T = 50;
        Tabu tabu = new Tabu(inst);
        List<Route> re = tabu.solve(1000, 10);
        System.out.println(new Gson().toJson(re));
    }

}
