package daniel.yyf.er017.november.guanjinghome;

import java.util.ArrayList;

public class Route {
	public ArrayList<Integer> seq; //the node sequence of a route which starts from and ends at the depot
	public int q; //the total demand of a route
	public double p; //the total profit of a route
	public double d; //the duration of a route
	
	public Route(){
		seq = new ArrayList<Integer>();
		seq.add(0);
		seq.add(0);
		q = 0;
		p = 0;
		d = 0;
	}
	
	public Route copy(){
		Route r = new Route();
		r.seq = (ArrayList<Integer>) seq.clone();
		r.q = q;
		r.p = p;
		r.d = d;
		return r;
	}
	
	public void check(Instance inst){
		double tq = 0;
		double td = inst.t[seq.get(0)][seq.get(1)];
		double tp = 0;
		for(int i = 1; i < seq.size() - 1; i++){
			int id = seq.get(i);
			tq += inst.q[id];
			tp += inst.p[id];
			td += inst.t[id][seq.get(i + 1)];
		}
		if(Math.abs(q - tq) > 1e-6){
			System.out.println("load computation error>>" + q + " " + tq);
			System.exit(0);
		}
		if(Math.abs(p - tp) > 1e-6){
			System.out.println("profit computation error>>" + p + " " + tp);
			System.exit(0);
		}
		if(Math.abs(d - td) > 1e-6){
			System.out.println("duration computation error>>" + d + " " + td);
			System.exit(0);
		}
		if(q > inst.Q + 1e-6){
			System.out.println("capacity error>>" + q + " " + inst.Q);
			System.exit(0);
		}
		if(d > inst.T + 1e-6){
			System.out.println("duration error>>" + d + " " + inst.T);
			System.exit(0);
		}
	}
}
