package daniel.yyf.er017.november.guanjinghome;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * @author danielyang
 * @Date 2017/12/11 11:21
 * 跑所有的测试用例
 */
public class Benchmark {
    public static final String RESULT_TEMPLATE = "\"%s-%d-%d-%d\",\"%.2f\",\"%s\"\n";
    public static final int[] Ms = {2, 3, 4};
    public static final int[][] QTs = {{50, 50}, {75, 75}, {100, 100}, {200, 200}};
    public static final String[] TEST_NAMES = {"p03", "p06", "p07", "p08", "p09", "p10", "p13",
            "p14", "p15", "p16"};
    static final String FILE_TEMPLATE = "/data/%s.cri";

    /**
     * 运行一个测试用例，返回结果
     *
     * @param inst
     * @param max_iter
     * @param tunure
     * @return
     */
    public String run(String testName, Instance inst, int max_iter, int tunure) {
        Tabu tabu = new Tabu(inst);
        List<Route> routes = tabu.solve(max_iter, tunure);
        double profit = 0;
        for (Route r : routes)
            profit += r.p;
        return String.format(RESULT_TEMPLATE, testName, inst.m, (int) inst.Q, (int) inst.T, profit,
                new Gson().toJson(routes));
    }

    public void test(String fileName) throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName)));
        for (String testName : TEST_NAMES) {
            for (int m : Ms) {
                for (int[] qt : QTs) {
                    Instance instance = new Instance(String.format(FILE_TEMPLATE, testName));
                    instance.m = m;
                    instance.Q = qt[0];
                    instance.T = qt[1];
                    String re = run(testName, instance, 1000, 10);
                    bufferedWriter.write(re);
                }
            }
        }
        bufferedWriter.close();
    }

    public static void main(String[] args) throws Exception {
        Benchmark benchmark = new Benchmark();
        benchmark.test("d:/resu.csv");
    }

    public void t() throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("d:/out.txt")));
        String[] s = {"03", "06", "07", "08", "09", "10", "13", "14", "15", "16"};
        Object[] tunues = {5, 10, 20, 30, 0.05, 0.1, 0.2, 0.3};
        int[] ms = {2, 3, 4};
        int[][] QTs = {{50, 50}, {75, 75}, {100, 100}, {200, 200}};
        double max_ave_profit = 0;
        Object tunue = 0;
        for (Object obj : tunues) {
            double profit = 0;
            int j = 0;
            for (String f : s) {
                String fileName = "/data/p" + f + ".cri";
                Instance instance = new Instance(fileName);
                for (int m : ms) {
                    for (int[] qt : QTs) {
                        instance.m = m;
                        instance.Q = qt[0];
                        instance.T = qt[1];
                        Tabu tabu = new Tabu(instance);
                        int tunuer = -1;
                        if (obj instanceof Integer) {
                            tunuer = (int) obj;
                        } else if (obj instanceof Double) {
                            tunuer = (int) (((double) obj) * instance.n);
                        }
                        List<Route> routes = tabu.solve(1000, tunuer);
                        for (Route r : routes) {
                            profit += r.p;
                        }
                        j++;
                    }
                }
            }
            profit /= j;
            if (profit > max_ave_profit) {
                max_ave_profit = profit;
                tunue = obj;
            }
            bufferedWriter.write("tunuere:" + obj + "," + "profit:" + profit + "\n");
            bufferedWriter.flush();
        }
        System.out.println("tunuere:" + tunue + "," + "profit:" + max_ave_profit);
    }


}
