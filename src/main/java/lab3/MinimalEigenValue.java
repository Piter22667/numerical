package lab3;

public class MinimalEigenValue {
    public MinimalEigenValue() {
    }

    public static double calculateInfinityNorm(double[][] matrix) {
        double maxSum = 0.0;
        double[][] var3 = matrix;
        int var4 = matrix.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            double[] row = var3[var5];
            double rowSum = 0.0;
            double[] var9 = row;
            int var10 = row.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                double value = var9[var11];
                rowSum += Math.abs(value);
            }

            maxSum = Math.max(maxSum, rowSum);
        }

        return maxSum;
    }

    public static double[][] createMatrixB(double[][] matrix, double norm) {
        int n = matrix.length;
        double[][] matrixB = new double[n][n];

        int i;
        int j;
        for(i = 0; i < n; ++i) {
            for(j = 0; j < n; ++j) {
                matrixB[i][j] = (i == j ? norm : 0.0) - matrix[i][j];
            }
        }
        System.out.println("Матриця B:\n");
        
        for(i = 0; i < n; ++i) {
            for(j = 0; j < n; ++j) {
                System.out.print(matrixB[i][j] + " ");
            }

            System.out.println();
        }
        return matrixB;
    }

    public static double findMaxEigenvalue(double[][] matrix, double tolerance, int maxIterations) {
        int n = matrix.length;
        double[] vector = new double[n];

        for(int i = 0; i < n; ++i) {
            vector[i] = 1.0;
        }

        double eigenvalue = 0.0;

        for(int iteration = 0; iteration < maxIterations; ++iteration) {
            double[] nextVector = multiplyMatrixVector(matrix, vector);
            double nextEigenvalue = normalize(nextVector);
            if (Math.abs(nextEigenvalue - eigenvalue) < tolerance) {
                break;
            }

            eigenvalue = nextEigenvalue;
            vector = nextVector;
        }

        return eigenvalue;
    }

    public static double[] multiplyMatrixVector(double[][] matrix, double[] vector) {
        int n = matrix.length;
        double[] result = new double[n];

        for(int i = 0; i < n; ++i) {
            result[i] = 0.0;

            for(int j = 0; j < n; ++j) {
                result[i] += matrix[i][j] * vector[j];
            }
        }

        return result;
    }

    public static double normalize(double[] vector) {
        double max = Math.abs(vector[0]);

        int i;
        for(i = 1; i < vector.length; ++i) {
            double absValue = Math.abs(vector[i]);
            if (absValue > max) {
                max = absValue;
            }
        }

        for(i = 0; i < vector.length; ++i) {
            vector[i] /= max;
        }

        return max;
    }

    public static void main(String[] args) {
        double[][] matrix = new double[][]{{2.0, 0.0, 1.0, 0.0}, {0.0, 3.0, 1.0, 1.0}, {1.0, 1.0, 2.0, 0.0}, {0.0, 1.0, 0.0, 4.0}};
        double tolerance = 0.001;
        int maxIterations = 1000;
        double norm = calculateInfinityNorm(matrix);
        System.out.println("Норма матриці ||A||_∞: " + norm);
        double[][] matrixB = createMatrixB(matrix, norm);
        double maxEigenvalueB = findMaxEigenvalue(matrixB, tolerance, maxIterations);
        double minEigenvalueA = norm - maxEigenvalueB;
        System.out.println("Мінімальне власне значення матриці A: " + minEigenvalueA);
    }
}
