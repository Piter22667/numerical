package org.example;

import java.util.Arrays;

public class Zeidel {
    public static void gaussSeidel(double[][] A, double[] B, double[] X, int maxIterations, double epsilon) {
        int n = B.length;
        double[] prevX = new double[n];
        for (int i = 0; i < n; i++) {
            X[i] = B[i] / A[i][i];
            prevX[i] = X[i];
        }
        System.out.println("Початкове наближення: " + Arrays.toString(X));

        for (int iteration = 1; iteration <= maxIterations; iteration++) {
            System.out.println("\nІтерація " + iteration + ":");
            for (int i = 0; i < n; i++) {
                double sigma = 0;
                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        sigma += A[i][j] * X[j];
                    }
                }
                X[i] = (B[i] - sigma) / A[i][i];
            }
            System.out.println("Знайдені x: " + Arrays.toString(X));
            if (hasConverged(prevX, X, epsilon)) {
                System.out.println("Рішення досягло точності на ітерації " + iteration);
                return;
            }
            System.arraycopy(X, 0, prevX, 0, n);
        }
        System.out.println("Максимальна кількість ітерацій досягнута.");
    }

    public static boolean hasConverged(double[] prevX, double[] X, double epsilon) {
        for (int i = 0; i < X.length; i++) {
            if (Math.abs(X[i] - prevX[i]) >= epsilon) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        double[][] A = {
                {4, 0, 1, 0},
                {0, 3, 0, 2},
                {1, 0, 5, 1},
                {0, 2, 1, 4}
        };
        double[] B = {7, 14, 20, 23};
        double[] X = new double[B.length];
        int maxIterations = 100;
        double epsilon = 1e-6;
        gaussSeidel(A, B, X, maxIterations, epsilon);

        System.out.println("\nРозвязок: ");
        for (int i = 0; i < X.length; i++) {
            System.out.printf("x%d = %.6f%n", i + 1, X[i]);
        }
    }
}
