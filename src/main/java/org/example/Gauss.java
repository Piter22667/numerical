package org.example;

public class Gauss {
    private static final double EPSILON = 1e-10;
    public static void main(String[] args) {
        int n = 4;
        double[][] A = {
                {7, 2, 3, 0},
                {0, 3, 2, 6},
                {2, 5, 1, 0},
                {0, 1, 4, 2}
        };
        double[] b = {20, 36, 15, 22};
        int[] colTransform = new int[n];
        for (int i = 0; i < n; i++) {
            colTransform[i] = i;
        }
        double[] x = solve(A.clone(), b.clone(), n, colTransform);
        if (x != null) {
            double[] finalSolution = new double[n];
            for (int i = 0; i < n; i++) {
                finalSolution[colTransform[i]] = x[i];
            }

            System.out.println("\nРозв'язок: ");
            for (int i = 0; i < n; i++) {
                System.out.printf("x%d = %.6f%n", i + 1, finalSolution[i]);
            }
        }
    }

    public static double[] solve(double[][] A, double[] b, int n, int[] colPerm) {
        double[] x = new double[n];

        try {
            // Прямий хід
            for (int k = 0; k < n - 1; k++) {
                double maxVal = Math.abs(A[k][k]);
                int maxCol = k;

                for (int j = k + 1; j < n; j++) {
                    if (Math.abs(A[k][j]) > maxVal) {
                        maxVal = Math.abs(A[k][j]);
                        maxCol = j;
                    }
                }

                if (Math.abs(maxVal) < EPSILON) {
                    System.out.println("Матриця вироджена!");
                    return null;
                }

                if (maxCol != k) {
                    swapColumns(A, k, maxCol);
                    int tempCol = colPerm[k];
                    colPerm[k] = colPerm[maxCol];
                    colPerm[maxCol] = tempCol;

                    System.out.printf("Переставляємо стовпці %d <-> %d%n", k + 1, maxCol + 1);
                }

                for (int i = k + 1; i < n; i++) {
                    double factor = A[i][k] / A[k][k];
                    b[i] -= factor * b[k];

                    for (int j = k; j < n; j++) {
                        A[i][j] -= factor * A[k][j];
                    }
                }
            }
            // Зворотній хід
            for (int i = n - 1; i >= 0; i--) {
                double sum = 0;
                for (int j = i + 1; j < n; j++) {
                    sum += A[i][j] * x[j];
                }
                x[i] = (b[i] - sum) / A[i][i];
            }
            return x;
        } catch (Exception e) {
            System.out.println("Помилка при розв'язанні: " + e.getMessage());
            return null;
        }
    }

    private static void swapColumns(double[][] A, int i, int j) {
        for (int k = 0; k < A.length; k++) {
            double temp = A[k][i];
            A[k][i] = A[k][j];
            A[k][j] = temp;
        }
    }
}