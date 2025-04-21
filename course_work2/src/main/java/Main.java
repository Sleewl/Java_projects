import library.FuncInterf;
import library.RKF45;

public class Main {
    static final double ABSERR = 1e-4;
    static final double RELERR = 1e-4;
    static final int NEQN = 2;
    static final double H_PRINT = 0.02;
    static double H_INT = 0.002;
    static final double TIME_LIMIT = 0.4;

    static final FuncInterf f = (n, t, x, dx) -> {
        dx[0] = -40 * x[0] + 260 * x[1] + 1 / (10 * t * t + 1);
        dx[1] = 30 * x[0] - 270 * x[1] + Math.exp(-2 * t);
        return 0;
    };

    public static void main(String[] args) {
        double[] y = {0, 1};
        double[] yp = new double[NEQN];
        int iflag = 1;
        double t = 0;
        double tout = H_PRINT;

        System.out.println("RKF45, H_PRINT = 0.02");
        for (int i = 0; i < (int)(TIME_LIMIT / H_PRINT) + 1; i++) {
            System.out.printf("%7.5f  %7.5f  %7.5f\n",t, y[0], y[1]);
            RKF45 rkf45 = new RKF45(f, NEQN, y, yp,
                    t, tout, RELERR, ABSERR, H_PRINT,
                    500, 5000, iflag);
            rkf45.complete();

            t += H_PRINT;
            tout += H_PRINT;
        }

        System.out.println();
        System.out.println("Adams, H_INT = 0.002");
        y[0] = 0;
        y[1] = 1;
        yp[0] = 0;
        yp[1] = 0;
        t = 0;
        Adams3.complete(f, NEQN, t, y, yp, H_INT);

        System.out.println();
        System.out.println("Adams3, H_INT = 0.001");
        y[0] = 0;
        y[1] = 1;
        yp[0] = 0;
        yp[1] = 0;
        t = 0;
        Adams3.complete(f, NEQN, t, y, yp, H_INT / 2);
    }

    static class Adams3 {
        static void complete(FuncInterf f, int n, double t, double[] x, double[] dx, double h) {
            // Вычисляем дополнительные начальные условия для старта метода Адамса
            double[] f1 = new double[n];
            double[] f2 = new double[n];
            double[] f3 = new double[n];

            f.f(n, t, x, f1);
            RKF45 rkf45 = new RKF45(f, n, x, dx, t, t+h, RELERR, ABSERR, h, 500, 5000, 1);
            rkf45.complete();
            t += h;

            f.f(n, t, x, f2);
            RKF45 rkf451 = new RKF45(f, n, x, dx, t, t+h, RELERR, ABSERR, h, 500, 5000, 1);
            rkf451.complete();
            t += h;
            f.f(n, t, x, f3);

            // Приступаем к вычислениям непосредственно методом Адамса
            for (int i = 0; i < (int)(TIME_LIMIT / h); i++) {
                if ((int)(t * 1000) % (int)(H_PRINT * 1000) == 0) {
                    System.out.printf("%7.5f  %7.5f  %7.5f\n",t, x[0], x[1]);
                }
                x[0] += h / 12 * (23 * f3[0] - 16 * f2[0] + 5 * f1[0]);
                x[1] += h / 12 * (23 * f3[1] - 16 * f2[1] + 5 * f1[1]);

                // Сдвигаем предыдущие значения
                f1[0] = f2[0];
                f1[1] = f2[1];

                f2[0] = f3[0];
                f2[1] = f3[1];

                f.f(n, t, x, f3);
                t += h;
            }
        }
    }
}
