package org.example;

public class TrianglePlusSimpson {

    public static void main(String[] args) {
        int[] nodes = {2, 3, 4, 5, 6};

        // Значення функції у вузлах
        double[] values = new double[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            values[i] = func(nodes[i]);
        }
        double[] coefficients = new double[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            coefficients[i] = integrateLagrange(nodes, i);
        }

        double integral = 0;
        for (int i = 0; i < nodes.length; i++) {
            integral += coefficients[i] * values[i];
        }

        System.out.println("Квадратурна формула інтерполяційного типу:");
        System.out.printf("Наближене значення інтегралу: %.5f%n", integral);

        int precisionDegree = findPrecision(nodes, coefficients);
        System.out.println("Алгебраїчний степінь точності: " + precisionDegree);

        System.out.println("\nНаближене обчислення інтегралу методами:");

        double midpointResult = integrateMidpoint(2, 6, 1000);
        System.out.printf("Метод середніх прямокутників: %.5f%n", midpointResult);


        double simpsonResult = integrateSimpson(2, 6, 1000);
        System.out.printf("Метод Сімпсона: %.5f%n", simpsonResult);
    }

    public static double func(double x) {
        return Math.pow(x, 7) + 2 * Math.pow(x, 5) + 3 * Math.pow(x, 3) - 2;
    }

    public static double integrateLagrange(int[] nodes, int i) {
        double h = 0.001;
        double sum = 0;

        for (double x = 2; x <= 6; x += h) {
            double lagrange = 1.0;
            for (int j = 0; j < nodes.length; j++) {
                if (i != j) {
                    lagrange *= (x - nodes[j]) / (nodes[i] - nodes[j]);
                }
            }
            sum += lagrange * h;
        }
        return sum;
    }

    public static int findPrecision(int[] nodes, double[] coefficients) {
        double epsilon = 1e-3;

        for (int degree = 0; degree <= nodes.length; degree++) {
            // Точне значення інтегралу
            double exactIntegral = (Math.pow(6, degree + 1) - Math.pow(2, degree + 1)) / (degree + 1);

            // Наближене значення за квадратурною формулою
            double approximateIntegral = 0;
            for (int i = 0; i < nodes.length; i++) {
                approximateIntegral += coefficients[i] * Math.pow(nodes[i], degree);
            }

            // Перевірка відносної похибки
            double relativeError = Math.abs((exactIntegral - approximateIntegral) / exactIntegral);
            System.out.printf("Degree %d: exact=%.10f, approx=%.10f, error=%.10f%n",
                    degree, exactIntegral, approximateIntegral, relativeError);
            if (relativeError > epsilon) {
                return degree - 1;
            }
        }
        return nodes.length - 1;
    }

    public static double integrateMidpoint(double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0;

        for (int i = 0; i < n; i++) {
            double x = a + (i + 0.5) * h;
            sum += func(x);
        }
        return sum * h;
    }

    public static double integrateSimpson(double a, double b, int n) {
        if (n % 2 != 0) {
            n++;
        }
        double h = (b - a) / n;
        int n2 = n/2;
        double sum = func(a);
        for (int i = 1; i <= n2; i++) {
            sum += 4 * func(a + (2*i-1)*h);
        }
        for (int i = 1; i <= n2-1; i++) {
            sum += 2 * func(a + 2*i*h);
        }
        sum += func(b);
        return (h/3) * sum;
    }
}