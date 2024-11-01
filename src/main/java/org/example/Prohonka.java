package org.example;

public class Prohonka {
    public static void main(String[] args) {

        double[][] A = {
                {1, 2, 0},
                {2, 2, 3},
                {0, 3, 2}
        };
        double[] f = {5, 15, 12};
        int n = f.length;
        double[] alpha = new double[n];
        double[] beta = new double[n];
        alpha[0] = -A[0][1] / A[0][0];
        beta[0] = f[0] / A[0][0];

        for (int i = 1; i < n - 1; i++) {
            double denominator = A[i][i] + A[i][i - 1] * alpha[i - 1];
            alpha[i] = -A[i][i + 1] / denominator;
            beta[i] = (f[i] - A[i][i - 1] * beta[i - 1]) / denominator;
        }

        beta[n - 1] = (f[n - 1] - A[n - 1][n - 2] * beta[n - 2]) / (A[n - 1][n - 1] + A[n - 1][n - 2] * alpha[n - 2]);

        double[] x = new double[n];
        x[n - 1] = beta[n - 1]; // останнє значення x
        for (int i = n - 2; i >= 0; i--) {
            x[i] = alpha[i] * x[i + 1] + beta[i];
        }

        System.out.println("Розвязок :");
        for (int i = 0; i < n; i++) {
            System.out.printf("x%d = %.2f\n", i + 1, x[i]);
        }
    }
}
