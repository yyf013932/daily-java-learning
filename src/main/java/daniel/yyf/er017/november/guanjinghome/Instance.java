package daniel.yyf.er017.november.guanjinghome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Instance {
    public int n; //the number of nodes, including the depot
    public int m; //the number of vehicles
    public double Q; //the vehicle capacity
    public double T; //the maximum duration

    public double q[]; //the demands of customers
    public double p[]; //the profits of customers
    public double t[][]; //the travel times of the arcs

    public Instance(String file) throws IOException {
        //read the instance from file
        String str;
        BufferedReader din = new BufferedReader(new InputStreamReader(Instance.class
                .getResourceAsStream(file)));
        Scanner dcin = new Scanner(din);
        double x0;
        double y0;
        String temp;
        for (int i = 0; i < 3; i++)
            temp = dcin.next();
        m = dcin.nextInt();
        dcin.next();
        Q = dcin.nextDouble();
        dcin.next();
        T = dcin.nextDouble();
        dcin.next();
        x0 = dcin.nextDouble();
        y0 = dcin.nextDouble();
        dcin.next();
        n = dcin.nextInt() + 1;
        double[][] coord = new double[n][2];
        t = new double[n][n];
        p = new double[n];
        q = new double[n];
        coord[0][0] = x0;
        coord[0][1] = y0;
        dcin.next();
        q[0] = 0;
        p[0] = 0;
        for (int i = 1; i < n; i++) {
            coord[i][0] = dcin.nextDouble();
            coord[i][1] = dcin.nextDouble();
            q[i] = dcin.nextDouble();
            dcin.next();
            p[i] = dcin.nextDouble();
        }
        for (int i = 0; i < n; i++)
            for (int j = i; j < n; j++)
                t[i][j] = t[j][i] =
                        Math.sqrt((coord[i][0] - coord[j][0]) * (coord[i][0] - coord[j][0]) +
                                (coord[i][1] - coord[j][1]) * (coord[i][1] - coord[j][1]));
        dcin.close();
    }
}
