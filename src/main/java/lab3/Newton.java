package lab3;

public class Newton {
    public static void main(String[] args) {
        double x0 = 0.0;
        double y0 = 0.5;
        newtonMethod(x0, y0);
    }

    private static void newtonMethod(double x0, double y0) {
        double x = x0;
        double y = y0;
        int iteration = 0;

        while(true) {
            double f1 = Math.sin(2.0 * x - y) - 1.2 * x - 0.4;
            double f2 = 0.8 * x * x + 1.5 * y * y - 1.0;
            double norm = Math.sqrt(f1 * f1 + f2 * f2);
            if (norm < 0.02) {
                System.out.printf("Розв'язок : x = %.5f, y = %.5f%n", x, y);
                System.out.printf("Кількість ітерацій: %d%n", iteration);
                return;
            }

            double df1_dx = 2.0 * Math.cos(2.0 * x - y) - 1.2;
            double df1_dy = -Math.cos(2.0 * x - y);
            double df2_dx = 1.6 * x;
            double df2_dy = 3.0 * y;
            double[][] jacobian = new double[][]{{df1_dx, df1_dy}, {df2_dx, df2_dy}};
            double[][] inverseJacobian = inverseMatrix(jacobian);

            double deltaX = -(inverseJacobian[0][0] * f1 + inverseJacobian[0][1] * f2);
            double deltaY = -(inverseJacobian[1][0] * f1 + inverseJacobian[1][1] * f2);
            x += deltaX;
            y += deltaY;
            ++iteration;
        }
    }

    private static double[][] inverseMatrix(double[][] matrix) {
        double det = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        if (Math.abs(det) < 1.0E-10) {
            return null;
        } else {
            double[][] inverse = new double[2][2];
            inverse[0][0] = matrix[1][1] / det;
            inverse[0][1] = -matrix[0][1] / det;
            inverse[1][0] = -matrix[1][0] / det;
            inverse[1][1] = matrix[0][0] / det;
            return inverse;
        }
    }
}
