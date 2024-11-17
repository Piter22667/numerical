package lab4;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.JFrame;

public class InterpolationPolinom {
    public static double originalFunction(double x) {
        return Math.pow(x, 7) + 2 * Math.pow(x, 5) + 3 * Math.pow(x, 3) - 2;
    }

    public static double lagrange(double[][] points, double x) {
        double result = 0;
        for (int i = 0; i < points[0].length; i++) {
            double term = points[1][i];
            for (int j = 0; j < points[0].length; j++) {
                if (i != j) {
                    // Множимо на (x - xj)/(xi - xj)
                    term *= (x - points[0][j]) / (points[0][i] - points[0][j]);
                }
            }
            result += term;
        }
        return result;
    }

    public static double findMaxDeviation(double[][] points, double start, double end, double step) {
        double maxDeviation = 0;
        double x = start;
        while (x <= end) {
            double originalValue = originalFunction(x);
            double lagrangeValue = lagrange(points, x);
            double deviation = Math.abs(originalValue - lagrangeValue);

            if (deviation > maxDeviation) {
                maxDeviation = deviation;
            }
            x += step;
        }
        return maxDeviation;
        //max|f(x) - L(x)|, де x ∈ [a,b]
    }

    public static void main(String[] args) {
        double[][] points = {
                {2, 3, 4, 5, 6}, // по осі x точки
                {214, 2752, 18622, 84748, 296134} // по осі y точки інтерполяції
        };

        XYSeries originalSeries = new XYSeries("Оригінальна функція");
        XYSeries lagrangeSeries = new XYSeries("Поліном Лагранжа");
        XYSeries pointsSeries = new XYSeries("Точки інтерполяції");

        for (double x = 2; x <= 6; x += 0.1) {
            originalSeries.add(x, originalFunction(x));
            lagrangeSeries.add(x, lagrange(points, x));
        }
        for (int i = 0; i < points[0].length; i++) {
            pointsSeries.add(points[0][i], points[1][i]);
        }
        double maxDeviation = findMaxDeviation(points, 2, 6, 0.01);
        double relativeError = (maxDeviation / originalFunction(6)) * 100;
        System.out.println("Максимальне відхилення: " + maxDeviation);
        System.out.println("Відносна похибка: " + relativeError + "%");

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(originalSeries);
        dataset.addSeries(lagrangeSeries);
        dataset.addSeries(pointsSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                " ", "x", "y", dataset
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesLinesVisible(2, false);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);

        plot.setRenderer(renderer);
        JFrame frame = new JFrame(" ");
        frame.add(new ChartPanel(chart));
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}