import java.util.*;

public class Measurement {
    static private List<Measurement> measurementList = new ArrayList<>();
    Integer value;
    Date time;

    public Measurement(Integer value, Date time) {
        this.value = value;
        this.time = time;

        measurementList.add(this);
    }

    @Override
    public String toString() {
        return String.format("%s: %d", time.toString(), value);
    }

    public Integer getValue() {
        return value;
    }

    public static void clear() {
        Measurement.measurementList.clear();
    }

    public static OptionalInt getMax() {
        return measurementList.stream()
                .mapToInt(measurement -> measurement.value)
                .max();
    }

    public static OptionalInt getMin() {
        return measurementList.stream()
                .mapToInt(measurement -> measurement.value)
                .min();
    }

    public static OptionalDouble getAvg() {
        return measurementList.stream()
                            .mapToInt(measurement -> measurement.value)
                            .average();
    }
}
