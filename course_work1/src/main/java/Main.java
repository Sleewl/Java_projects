import library.Decomp;
import library.Solve;

public class Main {
    static final int N = 6;
    static double[][] A = new double[N][N];
    static final double[] g = new double[N];
    static final double[] alphas = {0, 0.25, 0.49, 0.499};
    static double[] z = new double[N];
    static {
        for (int i = 0; i < N; i++) {
            g[i] = Math.pow(2, i-2);
        }
    }

    public static void main(String[] args) {
        for (double alpha: alphas) {
            initMatrix(A, alpha);
            initZVector(alpha);
            int[] pivot = new int[N];

            Decomp decomp = new Decomp(N, N, A, 0, pivot, 0);
            decomp.decomp();
            Solve solve = new Solve(N, N, A, z, pivot);
            solve.solve();
            double[] diff = subtructVectors(z, g);
            System.out.println("-----------------------------------");
            System.out.println("Param value: " + alpha);
            System.out.println("Param error: " + getVectNorm(diff) / getVectNorm(g));
            System.out.println("Cond: " + decomp.getCond());
            System.out.println("-----------------------------------");
        }
    }

    static void initMatrix(double[][] A, double param) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) {
                    A[i][j] = 1;
                } else if (i > j) {
                    A[i][j] = param;
                } else {
                    A[i][j] = j - i + 1;
                }
            }
        }
    }

    static void printVector(double[] vector) {
        for (int i = 0; i < vector.length; i++) {
            System.out.print(vector[i] + " ");
        }
        System.out.println();
    }

    static void printMatrix(double[][] A) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }
    }

    static double getVectNorm(double[] vect) {
        double max = 0; // since norm >= 0
        for (int i = 0; i < vect.length; i++) {
            max = Math.max(max, Math.abs(vect[i]));
        }
        return max;
    }

    static void initZVector(double param) {
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < i; j++) {
                z[i] += g[j];
            }
            z[i] *= param;

            for (int j = i; j < N; j++) {
                z[i] += (j - i + 1) * g[j];
            }
        }
    }

    static double[] subtructVectors(double[] vect1, double[] vect2) {
        double[] result = new double[N];
        for (int i = 0; i < vect1.length; i++) {
            result[i] = vect1[i] - vect2[i];
        }
        return result;
    }
}
