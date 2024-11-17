package lab3;

public class JacobiAprroximation {
    private static final double[][] A = new double[][]{
            {2.0, 0.0, 1.0, 0.0},
            {0.0, 3.0, 1.0, 1.0},
            {1.0, 1.0, 2.0, 0.0},
            {0.0, 1.0, 0.0, 4.0}};
    static int dimension = 4;

    public JacobiAprroximation() {
    }

    public static void main(String[] args) {
        double[][] matrix = new double[A.length][];
        for (int i = 0; i < A.length; i++) {
            matrix[i] = A[i].clone();
        }
        double[][] eigenVectors = jacobiMethod(matrix);
        System.out.println("Наближення до власних значень:");

        for(int i = 0; i < dimension; ++i) {
            System.out.printf("%.5f ", matrix[i][i]);
        }

        System.out.println();
    }

    public static double[][] jacobiMethod(double[][] matrix) {
        double[][] eigenVectors = new double[dimension][dimension];

        for(int i = 0; i < dimension; ++i) {
            eigenVectors[i][i] = 1.0;
        }

        boolean converge = false;
        int iteration = 0;

        while(!converge) {
            ++iteration;
            int p = 0;
            int q = 1;
            double max = Math.abs(matrix[p][q]);

            for(int i = 0; i < dimension - 1; ++i) {
                for(int j = i + 1; j < dimension; ++j) {
                    if (Math.abs(matrix[i][j]) > max) {
                        max = Math.abs(matrix[i][j]);
                        p = i;
                        q = j;
                    }
                }
            }

            double epsilon = 1.0E-6;
            if (max < epsilon) {
                converge = true;
                System.out.println("\nФінальна матриця після перетворень:");
                for (int i = 0; i < dimension; i++) {
                    for (int j = 0; j < dimension; j++) {
                        System.out.printf("%8.10f ", matrix[i][j]);
                    }
                    System.out.println();
                }
                break;
            }

            double fi = 0.5 * Math.atan2(2.0 * matrix[p][q], matrix[q][q] - matrix[p][p]);
            double cos = Math.cos(fi);
            double sin = Math.sin(fi);
            double[][] U = new double[dimension][dimension];

            for(int i = 0; i < dimension; ++i) {
                U[i][i] = 1.0;
            }

            U[p][p] = cos;
            U[q][q] = cos;
            U[p][q] = -sin;
            U[q][p] = sin;
            double[][] newMatrix = new double[dimension][dimension];

            int i;
            int j;
            int k;
            for(i = 0; i < dimension; ++i) {
                for(j = 0; j < dimension; ++j) {
                    for(k = 0; k < dimension; ++k) {
                        newMatrix[i][j] += U[i][k] * matrix[k][j];
                    }
                }
            }

            for(i = 0; i < dimension; ++i) {
                for(j = 0; j < dimension; ++j) {
                    matrix[i][j] = 0.0;

                    for(k = 0; k < dimension; ++k) {
                        matrix[i][j] += newMatrix[i][k] * U[j][k];
                    }
                }
            }

            for(i = 0; i < dimension; ++i) {
                double uip = eigenVectors[i][p];
                double uiq = eigenVectors[i][q];
                eigenVectors[i][p] = cos * uip - sin * uiq;
                eigenVectors[i][q] = sin * uip + cos * uiq;
            }
        }

        System.out.println("Загальна кількість ітерцій: " + iteration);
        return eigenVectors;
    }
}
