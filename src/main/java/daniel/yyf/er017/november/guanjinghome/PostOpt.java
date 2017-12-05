package daniel.yyf.er017.november.guanjinghome;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public void postOpt(List<Route> s) {
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
        for (int i = 1; i < inst.n; i++) {
            //只处理路径中未出现的用户
            if (visited[i])
                continue;
            List<RouteSubset> routeSubsets = new ArrayList<>();
            //遍历每一条路径
            for (int j = 0; j < s.size(); j++) {
                routeSubsets = generateAllSubsets(s.get(j));
                boolean added = false;
                for (int k = 0; k < routeSubsets.size(); k++) {
                    //不处理当前用户比子集的profit低的情况
                    if (routeSubsets.get(k).p >= inst.p[i])
                        continue;
                    Route newRoute = s.get(j).copy();
                    for (int ri : routeSubsets.get(k).routeIndexes) {
                        removal.remove(newRoute, ri);
                    }
                    int insertPosition = insertion.searchInsertPosition(newRoute, i);
                    if (insertPosition != -1) {
                        added = true;
                        tp += inst.p[i] - routeSubsets.get(k).p;
                        insertion.insert(newRoute, insertPosition, i);
                        s.set(j, newRoute);
                        System.out.println("================================");
                        System.out.println("route:" + j);
                        System.out.println("remove:" + routeSubsets.get(k).routeIndexes.stream()
                                .map(s.get(j).seq::get).collect(Collectors.toList()) + "\t"
                                + routeSubsets.get(k).p);
                        System.out.println("insert:" + i);
                        System.out.println("total profit:" + tp);
                        System.out.println("================================");
                        break;
                    }
                }
                if (added)
                    break;
            }

        }
    }

    /**
     * 生成某个路径的所有子集，按照profit的升序排列
     *
     * @param route
     * @return
     */
    private List<RouteSubset> generateAllSubsets(Route route) {
        List<RouteSubset> routeSubsets = new ArrayList<>();
        routeSubsets.add(new RouteSubset());
        //除去两个源节点
        for (int i = 1; i < route.seq.size() - 1; i++) {
            List<RouteSubset> tem = new ArrayList<>();
            for (RouteSubset routeSubset : routeSubsets) {
                RouteSubset toGenerate = new RouteSubset();
                for (int j : routeSubset.routeIndexes) {
                    toGenerate.append(j, inst.p[route.seq.get(j)]);
                }
                toGenerate.append(i, inst.p[route.seq.get(i)]);
                tem.add(toGenerate);
            }
            routeSubsets.addAll(tem);
        }
        //升序排列
        routeSubsets.sort(new Comparator<RouteSubset>() {
            @Override
            public int compare(RouteSubset o1, RouteSubset o2) {
                return ((Double) o2.p).compareTo(o1.p);
            }
        });
        return routeSubsets;
    }
}

class RouteSubset {
    List<Integer> routeIndexes = new ArrayList<>();
    double p = 0.0;

    public void append(int index, double profit) {
        int i = 0;
        //插入的时候降大的index放在前面，以免在删除的时候先删除了小的index会导致大的index无法删除（越界或错误）
        while (i < routeIndexes.size() && routeIndexes.get(i) > index) {
            i++;
        }
        routeIndexes.add(i, index);
        p += profit;
    }


}
