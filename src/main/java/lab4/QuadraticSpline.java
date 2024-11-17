package lab4;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;

public class QuadraticSpline extends JFrame {
    private double a1, b1, c1;  // коефіцієнти полінома P1(x)
    private double a2, b2, c2;  // коефіцієнти поліномаг P2(x)

    public static void main(String[] args) {
        QuadraticSpline spline = new QuadraticSpline();
        spline.calculateSpline();
        spline.createChart();
        spline.printResults();
    }

    private void calculateSpline() {
        // Вузли інтерполяції
        double[] xNodes = {2, 4, 6};
        double[] yNodes = new double[3];

        // Обчислюємо значення функції у вузлах
        for (int i = 0; i < 3; i++) {
            yNodes[i] = originalFunction(xNodes[i]);
        }

        // коефіцієнти для P1
        c1 = yNodes[0];  // P_1(2) = f(2)
        b1 = derivative(2);  // P_1'(2) = f'(2)
        a1 = (yNodes[1] - 2*b1 - c1)/2;  // P_1(4) = f(4)

        // коефіцієнти для P2
        c2 = yNodes[1];  // P_2(4) = f(4)
        b2 = 2*a1 + b1;   // умова неперервності похідної P_1'(4) = P2'(4)
        a2 = (yNodes[2] - 2*b2 - c2)/2;  // з P_2(6) = f(6)
    }

    private void createChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries originalSeries = new XYSeries("Оригінальна функція f(x)");
        XYSeries p1Series = new XYSeries("Поліном P_1(x)");
        XYSeries p2Series = new XYSeries("Поліном P_2(x)");
        XYSeries nodesSeries = new XYSeries("Вузли інтерполяції");

        // Заповнюємо дані
        for (double x = 2; x <= 6; x += 0.01) {
            originalSeries.add(x, originalFunction(x));
            if (x <= 4) p1Series.add(x, P1(x));
            if (x >= 4) p2Series.add(x, P2(x));
        }

        double[] nodes = {2, 4, 6};
        for (double node : nodes) {
            nodesSeries.add(node, originalFunction(node));
        }

        dataset.addSeries(originalSeries);
        dataset.addSeries(p1Series);
        dataset.addSeries(p2Series);
        dataset.addSeries(nodesSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                " ",
                "x", "y",
                dataset
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesShapesVisible(3, true);
        renderer.setSeriesLinesVisible(3, false);
        renderer.setSeriesPaint(3, Color.RED);

        setSize(800, 600);
        setContentPane(new ChartPanel(chart));
        setVisible(true);
    }

    private double originalFunction(double x) {
        return Math.pow(x, 7) + 2*Math.pow(x, 5) + 3*Math.pow(x, 3) - 2;
    }

    private double derivative(double x) {
        return 7*Math.pow(x, 6) + 10*Math.pow(x, 4) + 9*Math.pow(x, 2);
    }

    private double P1(double x) {
        return a1/2 * Math.pow(x - 2, 2) + b1 * (x - 2) + c1;
    }

    private double P2(double x) {
        return a2/2 * Math.pow(x - 4, 2) + b2 * (x - 4) + c2;
    }

    private double calculateMaxDeviation() {
        double maxDeviation = 0;
        for (double x = 2; x <= 4; x += 0.01) {
            maxDeviation = Math.max(maxDeviation,
                    Math.abs(P1(x) - originalFunction(x)));
        }
        for (double x = 4; x <= 6; x += 0.01) {
            maxDeviation = Math.max(maxDeviation,
                    Math.abs(P2(x) - originalFunction(x)));
        }
        return maxDeviation;
    }

    private void printResults() {
        System.out.println("Коефіцієнти P1(x):");
        System.out.printf("a1 = %.2f%n", a1);
        System.out.printf("b1 = %.2f%n", b1);
        System.out.printf("c1 = %.2f%n", c1);

        System.out.println("\nКоефіцієнти P2(x):");
        System.out.printf("a2 = %.2f%n", a2);
        System.out.printf("b2 = %.2f%n", b2);
        System.out.printf("c2 = %.2f%n", c2);

        System.out.println("\nПеревірка умов:");
        System.out.printf("P1(2) = %.2f, f(2) = %.2f%n", P1(2), originalFunction(2));
        System.out.printf("P1(4) = %.2f, f(4) = %.2f%n", P1(4), originalFunction(4));
        System.out.printf("P2(4) = %.2f, f(4) = %.2f%n", P2(4), originalFunction(4));
        System.out.printf("P2(6) = %.2f, f(6) = %.2f%n", P2(6), originalFunction(6));

        System.out.println("\nПеревірка похідних:");
        System.out.printf("P1'(2) = %.2f, f'(2) = %.2f%n", b1, derivative(2));
        System.out.printf("P1'(4) = %.2f%n", 2*a1 + b1);
        System.out.printf("P2'(4) = %.2f%n", b2);
        System.out.printf("P2'(6) = %.2f, f'(6) = %.2f%n", 2*a2 + b2, derivative(6));
        System.out.printf("\nМаксимальне відхилення: %.2f%n", calculateMaxDeviation());
    }
}