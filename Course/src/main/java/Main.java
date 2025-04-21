import library.FuncInterf;
import library.Quanc8;
import library.RKF45;
import library.Zeroin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

public class Main {
    static final double mu = 1 / 82.45;
    static final double mu1 = 1 - mu;
    static final double A = 1.2;
    static final double D = -1.04935751;
    static double B, C, xSolve;
    static final double left = 1.5;
    static final double right = 2;
    static final double ABSERR = 1e-7;
    static final double RELERR = 1e-7;

    static final double H_PRINT = 0.1;
    static final double TIME_LIMIT = 20;
    static final int NEQN = 4;

    static final Function<Double, Double> intFunc = new Function<Double, Double>() {
        @Override
        public Double apply(Double x) {
            return Math.sqrt(2 * x*x) / (1 + Math.cos(2 * x));
        }
    };

    static final Function<Double, Double> equation = new Function<Double, Double>() {
        @Override
        public Double apply(Double x) {
            return Math.log10(x) - 1/(x*x);
        }
    };

    static final FuncInterf system = new FuncInterf() {

        @Override
        public int f(int n, double t, double[] x, double[] dx) {
            dx[0] = x[2];
            dx[1] = x[3];
            dx[2] = 2 * x[3] + x[0] - mu1 * (x[0] + mu) / Math.pow(r1(x[0], x[1]), 3) - mu * (x[0]-mu1) / Math.pow(r2(x[0], x[1]), 3);
            dx[3] = -2 * x[2] + x[1] - mu1 * x[1] / Math.pow(r1(x[0],x[1]), 3) - mu*x[1] / Math.pow(r2(x[0],x[1]), 3);
            return 0;
        }
    };

    public static void main(String[] args) {
        Zeroin zeroin = new Zeroin(equation, left, right, 1e-7);
        xSolve = zeroin.complete();

        C = Math.pow(xSolve - 1.896651, 2);

        Quanc8 quanc8 = new Quanc8(intFunc, 0, 1, ABSERR, RELERR);
        quanc8.calculate();
        B = Math.pow(quanc8.getRESULT() - 1.2162863, 4);

        double[] xd = {A, C, B, D};
        double[] xpd = new double[NEQN];
        double t = 0;
        double tout = H_PRINT;
        int iflag = 1;
        String csvFile = "output.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            for (int i = 0; i < (int)(TIME_LIMIT / H_PRINT) + 1; i++) {
                String formatted = String.format("%9f; %15e; %15e; %15e; %15e", t, xd[0], xd[1], xd[2], xd[3]);
                writer.write(formatted);
                writer.newLine();
                RKF45 rkf45 = new RKF45(system, NEQN, xd, xpd, t, tout, RELERR, ABSERR, H_PRINT, 1000, 15000, iflag);
                rkf45.complete();

                t += H_PRINT;
                tout += H_PRINT;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static double r1(double x1, double x2) {
        return Math.sqrt(Math.pow(x1, mu) + Math.pow(x2, 2));
    }

    static double r2(double x1, double x2) {
        return Math.sqrt(Math.pow(x1 - mu1,2) + Math.pow(x2, 2));
    }
}
