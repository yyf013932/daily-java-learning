package daniel.yyf.er017.november.guanjinghome;

import java.util.ArrayList;

//implement the insertion operator: insert a customer to the solution
public class Insertion {

    public Instance inst;
    public int[] tabu_list;
    public int tabu_tenure;

    public Insertion(Instance _inst, int[] _tabu_list, int _tabu_tenure) {
        inst = _inst;
        tabu_list = _tabu_list;
        tabu_tenure = _tabu_tenure;
    }

    //explore the neighborhood of the insertion operator
    //s is the current solution
    //iter is the current iteration of the tabu search
    //best_profit is the profit of the best solution
    //return an array ret
    //ret[0] is the customer to be inserted
    //ret[1] is the route where the customer is inserted
    //ret[2] is the place where the customer is inserted
    public int[] explore(ArrayList<Route> s, int iter, double best_profit) {
        boolean[] visited = new boolean[inst.n];
        double tp = 0;
        for (int i = 0; i < s.size(); i++) {
            Route r = s.get(i);
            tp += r.p;
            for (int j = 1; j < r.seq.size() - 1; j++) {
                visited[r.seq.get(j)] = true;
            }
        }
        double max_profit = 0;
        double min_duration = 1e10;
        int[] ret = new int[3];
        ret[0] = ret[1] = ret[2] = -1;
        for (int i = 1; i < inst.n; i++) {
            if (visited[i])
                continue;
            if (iter - tabu_list[i] < tabu_tenure && tp + inst.p[i] <= best_profit)
                continue;
            for (int j = 0; j < s.size(); j++) {
                Route r = s.get(j);
                if (r.q + inst.q[i] > inst.Q)
                    continue;
                for (int k = 0; k < r.seq.size() - 1; k++) {
                    int id1 = r.seq.get(k);
                    int id2 = r.seq.get(k + 1);
                    double duration_delta = inst.t[id1][i] + inst.t[i][id2] - inst.t[id1][id2];
                    if (r.d + duration_delta > inst.T)
                        continue;
                    if (inst.p[i] > max_profit + 1e-6 || inst.p[i] > max_profit - 1e-6 && min_duration > duration_delta) {
                        max_profit = inst.p[i];
                        min_duration = duration_delta;
                        ret[0] = i;
                        ret[1] = j;
                        ret[2] = k;
                    }
                }
            }
        }
        return ret;
    }

    public int searchInsertPosition(Route r, int cusId) {
        double min_duration = 1e10;
        int re = -1;
        for (int k = 0; k < r.seq.size() - 1; k++) {
            int id1 = r.seq.get(k);
            int id2 = r.seq.get(k + 1);
            double duration_delta = inst.t[id1][cusId] + inst.t[cusId][id2] - inst.t[id1][id2];
            if (r.q + inst.q[cusId] > inst.Q)
                continue;
            if (r.d + duration_delta > inst.T)
                continue;
            if (min_duration > duration_delta) {
                min_duration = duration_delta;
                re = k;
            }
        }
        return re;
    }

    public void insert(Route r, int pos2Insert, int cusId) {
        r.d += inst.t[r.seq.get(pos2Insert)][cusId] + inst.t[cusId][r.seq.get(pos2Insert + 1)] - inst.t[r.seq.get(pos2Insert)][r.seq.get(pos2Insert + 1)];
        r.seq.add(pos2Insert + 1, cusId);
        r.q += inst.q[cusId];
        r.p += inst.p[cusId];
    }
}
