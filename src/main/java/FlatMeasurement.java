import java.util.Date;

public class FlatMeasurement {
    Integer min;
    Integer max;
    Integer avg;
    Date date;

    public FlatMeasurement() {
        this.min = Measurement.getMin().isPresent() ? Measurement.getMin().getAsInt() : 0;
        this.max = Measurement.getMax().isPresent() ? Measurement.getMax().getAsInt() : 0;
        Double avg = Measurement.getAvg().isPresent() ? Measurement.getAvg().getAsDouble() : null;
        this.avg = avg != null ? avg.intValue() : 0;
        Measurement.clear();
        date = new Date();
    }

    public FlatMeasurement(Integer min, Integer max, Integer avg, Date date) {
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.date = date;
    }
}
