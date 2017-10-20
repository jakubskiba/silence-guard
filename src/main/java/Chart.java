import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Chart {
    public void drawChart(List<FlatMeasurement> measurements) {
        XYSeriesCollection collection = new XYSeriesCollection();

        XYSeries avg = new XYSeries("avg");
        XYSeries min = new XYSeries("min");
        XYSeries max = new XYSeries("max");
        for(FlatMeasurement measurement : measurements) {
            avg.add(measurements.indexOf(measurement), measurement.avg.doubleValue());
            min.add(measurements.indexOf(measurement), measurement.min.doubleValue());
            max.add(measurements.indexOf(measurement), measurement.max.doubleValue());
        }

        collection.addSeries(avg);
        collection.addSeries(min);
        collection.addSeries(max);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Silence level",   // chart title
                "Measurement",
                "Level",
                collection,
                PlotOrientation.VERTICAL,
                false,             // include legend
                true,
                false );

        int width = 800;    /* Width of the image */
        int height = 600;   /* Height of the image */
        File pieChart = new File( "Chart.jpeg" );
        try {
            ChartUtilities.saveChartAsJPEG(pieChart , chart , width , height );
        } catch (IOException e) {
            Logger.createLogger().error(e.toString());
        }

    }

    public static void main(String[] args) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Scanner in = new Scanner(System.in);

        Date from = null;
        Date to = null;
        try {
            from = dt.parse(in.nextLine().trim());
            to = dt.parse(in.nextLine().trim());
        } catch (ParseException e) {
            System.out.println(e.toString());
        }

        System.out.print("getting data...");
        Connection connection = DBConnection.getConnection("measurements.db");
        List<FlatMeasurement> list = new MeasurementDAO(connection).getSavedMeasurements(from, to);
        System.out.println("done.");
        System.out.print("drawing...");
        new Chart().drawChart(list);
        System.out.println("done.");
    }
}
