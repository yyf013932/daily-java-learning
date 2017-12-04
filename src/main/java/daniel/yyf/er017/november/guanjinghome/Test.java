package daniel.yyf.er017.november.guanjinghome;

import com.google.gson.Gson;

import java.io.IOException;
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
        inst.Q = 100;
        inst.T = 100;
        Tabu tabu = new Tabu(inst);
        List<Route> re = tabu.solve(200, 100);
        System.out.println(new Gson().toJson(re));
    }

}
