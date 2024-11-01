package org.example;

public class Gauss {
    public static void main(String[] args) {
        int n = 4;
        double[][] A = {
                {7, 2, 3, 0},
                {0, 3, 2, 6},
                {2, 5, 1, 0},
                {0, 1, 4, 2}
        };
        double[] b = {20, 36, 15, 22};
        double[] x = new double[n];
        System.out.println("Matrix A:");
        //

        System.out.println("\nVector b:");
        for (int i = 0; i < n; i++) {
            System.out.printf("%.2f\n", b[i]);
        }
    }
}