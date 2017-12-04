package daniel.yyf.er017.november.guanjinghome;

import java.util.ArrayList;
import java.util.List;

public class Tabu {

    public Instance inst;

    public Tabu(Instance _inst) {
        inst = _inst;
    }

    public List<Route> solve(int max_iter, int tabu_tenure) {
        ArrayList<Route> best_solution = new ArrayList<Route>();
        double best_profit = 0;
        //construct an initial solution
        double sp = 0;
        ArrayList<Route> s = new ArrayList<Route>();
        for (int i = 0; i < inst.m; i++) {
            s.add(new Route());
        }
        //用于存储是否已经遍历了节点
        int[] tabu_list = new int[inst.n];
        for (int i = 0; i < inst.n; i++) {
            tabu_list[i] = -1000000000;
        }
        Insertion insertion = new Insertion(inst, tabu_list, tabu_tenure);
        Removal removal = new Removal(inst, tabu_list, tabu_tenure);
        PostOpt postOpt = new PostOpt(inst, removal, insertion);
        int iter = 0;
        while (iter < max_iter) {
            int[] ir = insertion.explore(s, iter, best_profit);
            int[] rr = null;
            if (ir[0] == -1) {
                rr = removal.explore(s, iter);
            }
            if (ir[0] == -1 && rr[0] == -1) {
                System.out.println("no feasible solution");
                break;
            }
            if (ir[0] != -1) {
                //TODO 1 有bug，我认为tabu_list与tabu_tunure是为了不让短期删除的节点加入route,这意味着在加入的过程中不应该更改这个list
//				tabu_list[ir[0]] = iter;
                Route r = s.get(ir[1]);
                insertion.insert(r, ir[2], ir[0]);
//                r.d += inst.t[r.seq.get(ir[2])][ir[0]] + inst.t[ir[0]][r.seq.get(ir[2] + 1)] - inst.t[r.seq.get(ir[2])][r.seq.get(ir[2] + 1)];
//                r.seq.add(ir[2] + 1, ir[0]);
//                r.q += inst.q[ir[0]];
//                r.p += inst.p[ir[0]];
                sp += inst.p[ir[0]];
            } else {
                Route r = s.get(rr[0]);
                int id = r.seq.get(rr[1]);
                removal.remove(r, rr[1]);
                tabu_list[id] = iter;
//                r.d -= inst.t[r.seq.get(rr[1] - 1)][id] + inst.t[id][r.seq.get(rr[1] + 1)] - inst.t[r.seq.get(rr[1] - 1)][r.seq.get(rr[1] + 1)];
//                r.seq.remove(rr[1]);
//                r.q -= inst.q[id];
//                r.p -= inst.p[id];
                sp -= inst.p[id];
            }
            if (sp > best_profit) {
                best_profit = sp;
                best_solution.clear();
                for (int i = 0; i < s.size(); i++) {
                    best_solution.add(s.get(i).copy());
                }
            }
            String operation = "insertion";
            if (ir[0] == -1)
                operation = "removal";
            iter++;
            System.out.println(iter + "\t" + operation + "\t" + sp + "\t" + best_profit);
            //check solution
            double tsp = 0;
            for (int i = 0; i < s.size(); i++) {
                s.get(i).check(inst);
                tsp += s.get(i).p;
            }
            if (Math.abs(tsp - sp) > 1e-6) {
                System.out.println("solution profit error>>>" + tsp + " " + sp);
                System.exit(0);
            }
        }
        return postOpt.postOpt(best_solution);
    }
}
