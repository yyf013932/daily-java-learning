package daniel.yyf.er017.november.guanjinghome;

import java.util.ArrayList;
import java.util.List;

/**
 * @author danielyang
 * @Date 2017/12/4 16:21
 * 迭代完成之后最后进行的一次优化操作
 */
public class PostOpt {
    Instance inst;
    Removal removal;
    Insertion insertion;

    public PostOpt(Instance inst, Removal removal, Insertion insertion) {
        this.inst = inst;
        this.removal = removal;
        this.insertion = insertion;
    }

    public List<Route> postOpt(List<Route> s) {
        boolean[] visited = new boolean[inst.n];
        double tp = 0;
        for (int i = 0; i < s.size(); i++) {
            Route r = s.get(i);
            tp += r.p;
            for (int j = 1; j < r.seq.size() - 1; j++) {
                visited[r.seq.get(j)] = true;
            }
        }
        //遍历所有用户
        for (int i = 0; i < inst.n; i++) {
            //只处理路径中未出现的用户
            if (visited[i])
                continue;
            boolean added = true;
            List<RouteSubset> routeSubsets = new ArrayList<>();
            //遍历每一条路径
            for (int j = 0; j < s.size(); j++) {
                if (added)
                    routeSubsets = generateAllSubsets(s.get(i = j));
                added = false;
                for (int k = 0; k < routeSubsets.size(); k++) {
                    //不处理当前用户比子集的profit低的情况
                    if (routeSubsets.get(k).p > inst.p[i])
                        continue;
                    Route newRoute = s.get(j).copy();
                    for (int ri : routeSubsets.get(k).routeIndexs) {
                        removal.remove(newRoute, ri);
                    }
                }
            }

        }
        return null;
    }


    /**
     * 生成某个路径的所有子集，按照profit的升序排列
     *
     * @param route
     * @return
     */
    private List<RouteSubset> generateAllSubsets(Route route) {
        List<RouteSubset> routeSubsets = new ArrayList<>();
        //从0元集到n-2元集（除去两个源节点）
        for (int i = 0; i < route.seq.size() - 2; i++) {
        }
        return null;
    }
}

class RouteSubset {
    List<Integer> routeIndexs;
    double p;
}
