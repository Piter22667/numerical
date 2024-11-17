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
import java.util.ArrayList;
import java.util.List;

public class LinearSpline extends JFrame {
    private List<Double> xNodes;
    private List<Double> yNodes;
    private List<Double> aSide;
    private List<Double> bSide;

    public static void main(String[] args) {
        LinearSpline spline = new LinearSpline();
        spline.calculateSpline();
        spline.createChart();
        spline.printResults();
    }

    private void calculateSpline() {
        // Створюємо точки розбиття з кроком 0.5
        xNodes = new ArrayList<>();
        yNodes = new ArrayList<>();
        for (double x = 2.0; x <= 6.0; x += 0.5) {
            xNodes.add(x);
            yNodes.add(originalFunction(x));
        }

        // Обчислюємо коефіцієнти для кожного відрізка
        int n = xNodes.size() - 1;
        aSide = new ArrayList<>();
        bSide = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            double x1 = xNodes.get(i);
            double x2 = xNodes.get(i + 1);
            double y1 = yNodes.get(i);
            double y2 = yNodes.get(i + 1);

            // Знаходимо коефіцієнти
            double b = y1;
            double a = (y2 - y1) / (x2 - x1);
            aSide.add(a);
            bSide.add(b);
        }
    }

    private double originalFunction(double x) {
        return Math.pow(x, 7) + 2 * Math.pow(x, 5) + 3 * Math.pow(x, 3) - 2;
    }

    private double splineFunction(double x) {
        // Знаходимо відповідний відрізок
        for (int i = 0; i < xNodes.size() - 1; i++) {
            if (x >= xNodes.get(i) && x <= xNodes.get(i + 1)) {
                return aSide.get(i) * (x - xNodes.get(i)) + bSide.get(i);
            }
        }
        return 0;
    }

    private void createChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries originalSeries = new XYSeries("Оригінальна функція f(x)");
        XYSeries splineSeries = new XYSeries("Лінійний сплайн");
        XYSeries nodesSeries = new XYSeries("Вузли інтерполяції");

        for (double x = 2; x <= 6; x += 0.01) {
            originalSeries.add(x, originalFunction(x));
            splineSeries.add(x, splineFunction(x));
        }

        for (int i = 0; i < xNodes.size(); i++) {
            nodesSeries.add(xNodes.get(i), yNodes.get(i));
        }

        dataset.addSeries(originalSeries);
        dataset.addSeries(splineSeries);
        dataset.addSeries(nodesSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Лінійний сплайн",
                "x", "y",
                dataset
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesLinesVisible(2, false);
        renderer.setSeriesPaint(2, Color.RED);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new ChartPanel(chart));
        setVisible(true);
    }

    private void printResults() {
        System.out.println("Коефіцієнти a, b :");
        for (int i = 0; i < aSide.size(); i++) {
            System.out.printf("Відрізок [%.1f, %.1f]:%n", xNodes.get(i), xNodes.get(i + 1));
            System.out.printf("a = %.2f, b = %.2f%n", aSide.get(i), bSide.get(i));
        }
        double maxDeviation = 0;
        for (double x = 2; x <= 6; x += 0.01) {
            double deviation = Math.abs(splineFunction(x) - originalFunction(x));
            maxDeviation = Math.max(maxDeviation, deviation);
        }
        System.out.printf("%nМаксимальне відхилення: %.2f%n", maxDeviation);
    }
}