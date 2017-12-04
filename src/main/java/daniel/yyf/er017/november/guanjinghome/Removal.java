package daniel.yyf.er017.november.guanjinghome;

import java.util.ArrayList;

//implement the removal operator: remove a customer from the solution
public class Removal {


    public Instance inst;
    public int[] tabu_list;
    public int tabu_tenure;

    public Removal(Instance _inst, int[] _tabu_list, int _tabu_tenure) {
        inst = _inst;
        tabu_list = _tabu_list;
        tabu_tenure = _tabu_tenure;
    }

    //explore the neighborhood of the insertion operator
    //s is the current solution
    //iter is the current iteration of the tabu search
    //return an array ret
    //ret[0] is the route where the target customer is;
    //ret[1] is the place where the target customer is
    public int[] explore(ArrayList<Route> s, int iter) {
        double min_profit = 1e10;
        double max_duration = 0;
        int[] ret = new int[2];
        ret[0] = ret[1] = -1;
        for (int i = 0; i < s.size(); i++) {
            Route r = s.get(i);
            for (int j = 1; j < r.seq.size() - 1; j++) {
                int id1 = r.seq.get(j - 1);
                int id2 = r.seq.get(j);
                int id3 = r.seq.get(j + 1);
                if (iter - tabu_list[id2] < tabu_tenure)
                    continue;
                double duration_delta = inst.t[id1][id2] + inst.t[id2][id3] - inst.t[id1][id3];
                if (inst.p[id2] < min_profit - 1e-6 || inst.p[id2] < min_profit + 1e-6 && duration_delta > max_duration) {
                    min_profit = inst.p[id2];
                    max_duration = duration_delta;
                    ret[0] = i;
                    ret[1] = j;
                }
            }
        }
        return ret;
    }

    /**
     * 删除路径里指定的index位置的节点，返回新的Route
     *
     * @param r
     * @param indexes
     * @return
     */
    public void remove(Route r, int[] indexes) {
        for (int i : indexes) {
            remove(r, i);
        }
    }

    public void remove(Route r, int index) {
        int id = r.seq.get(index);
        r.d -= inst.t[r.seq.get(index - 1)][id] + inst.t[id][r.seq.get(index + 1)] - inst.t[r.seq.get(index - 1)][r.seq.get(index + 1)];
        r.seq.remove(index);
        r.q -= inst.q[id];
        r.p -= inst.p[id];
    }
}
