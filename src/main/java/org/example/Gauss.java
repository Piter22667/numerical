package org.example;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import java.util.Arrays;

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
        double[][] A1 = {
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
            System.out.println("Визначник : " + calculateDeterminant(A1, n));



//            System.out.println("\nОбернена матриця:");
//            double[][] inverseMatrix = findInverseMatrixUsingLU(A1);
//            for (double[] row : inverseMatrix) {
//                System.out.println(Arrays.toString(row));
//            }


            System.out.println("\nОбернена матриця:");
            double[][] inverseMatrix = findInverseMatrix(A1, n);
            for (double[] row : inverseMatrix) {
                System.out.println(Arrays.toString(row));
            }

            System.out.println("\nРозв'язок: ");
            for (int i = 0; i < n; i++) {
                System.out.printf("x%d = %.6f%n", i + 1, finalSolution[i]);
            }
        }
    }

    public static double calculateDeterminant(double[][] A, int n) {
        // лічильник кількості перестановок
        int swapCount = 0;
        double det = 1.0;
        double[][] matrix = deepCopy(A);

        for (int k = 0; k < n - 1; k++) {
            double maxVal = Math.abs(matrix[k][k]);
            int maxCol = k;

            for (int j = k + 1; j < n; j++) {
                if (Math.abs(matrix[k][j]) > maxVal) {
                    maxVal = Math.abs(matrix[k][j]);
                    maxCol = j;
                }
            }
            if (Math.abs(maxVal) < EPSILON) {
                return 0.0; // матриця вироджена
            }

            if (maxCol != k) {
                swapColumns(matrix, k, maxCol);
                swapCount++;
            }
            det *= matrix[k][k];

            for (int i = k + 1; i < n; i++) {
                double factor = matrix[i][k] / matrix[k][k];
                for (int j = k; j < n; j++) {
                    matrix[i][j] -= factor * matrix[k][j];
                }
            }
        }

        det *= matrix[n-1][n-1];
        return det * Math.pow(-1, swapCount);
    }

    private static double[][] deepCopy(double[][] matrix) {
        return Arrays.stream(matrix)
                .map(double[]::clone)
                .toArray(double[][]::new);
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

            for (int i = n - 1; i >= 0; i--) {
                double sum = 0;
                for (int j = i + 1; j < n; j++) {
                    sum += A[i][j] * x[j];
                }
                x[i] = (b[i] - sum) / A[i][i];
            }
            return x;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


//
//    public static double[][] findInverseMatrixUsingLU(double[][] A) {
//        RealMatrix matrix = MatrixUtils.createRealMatrix(A);
//        RealMatrix inverseMatrix = new LUDecomposition(matrix).getSolver().getInverse();
//        return inverseMatrix.getData();
//    }

    public static double[][] findInverseMatrix(double[][] A, int n) {
        if (Math.abs(calculateDeterminant(A, n)) < EPSILON) {
            System.out.println("Матриця вироджена! Оберненої не існує.");
            return null;
        }

        double[][] triangularA = deepCopy(A);

        double[][] E = new double[n][n];
        for (int i = 0; i < n; i++) {
            E[i][i] = 1.0;
        }

        // Прямий хід
        for (int k = 0; k < n - 1; k++) {
            // Вибір головного елементу
            double maxElement = Math.abs(triangularA[k][k]);
            int maxRow = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(triangularA[i][k]) > maxElement) {
                    maxElement = Math.abs(triangularA[i][k]);
                    maxRow = i;
                }
            }

            if (maxRow != k) {
                double[] tempA = triangularA[k];
                triangularA[k] = triangularA[maxRow];
                triangularA[maxRow] = tempA;
                double[] tempE = E[k];
                E[k] = E[maxRow];
                E[maxRow] = tempE;
            }

            // Перетворення рядків
            for (int i = k + 1; i < n; i++) {
                double factor = triangularA[i][k] / triangularA[k][k];

                for (int j = k; j < n; j++) {
                    triangularA[i][j] -= factor * triangularA[k][j];
                }
                for (int j = 0; j < n; j++) {
                    E[i][j] -= factor * E[k][j];
                }
            }
        }

        // Зворотній хід
        double[][] inverse = new double[n][n];
        for (int j = 0; j < n; j++) {
            double[] y = new double[n];
            for (int i = 0; i < n; i++) {
                y[i] = E[i][j];
            }
            // Розв'язуємо систему для j-го стовпця
            for (int i = n - 1; i >= 0; i--) {
                double sum = 0;
                for (int k = i + 1; k < n; k++) {
                    sum += triangularA[i][k] * inverse[k][j];
                }
                inverse[i][j] = (y[i] - sum) / triangularA[i][i];
            }
        }
        return inverse;
    }




    private static void swapColumns(double[][] A, int i, int j) {
        for (int k = 0; k < A.length; k++) {
            double temp = A[k][i];
            A[k][i] = A[k][j];
            A[k][j] = temp;
        }
    }
}