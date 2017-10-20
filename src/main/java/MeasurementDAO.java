import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.OptionalInt;

public class MeasurementDAO {
    Connection connection;
    Integer min;
    Integer max;
    Integer avg;

    public MeasurementDAO(Connection connection) {
        this.connection = connection;
        this.min = Measurement.getMin().isPresent() ? Measurement.getMin().getAsInt() : 0;
        this.max = Measurement.getMax().isPresent() ? Measurement.getMax().getAsInt() : 0;
        Double avg = Measurement.getAvg().isPresent() ? Measurement.getAvg().getAsDouble() : null;
        this.avg = avg != null ? avg.intValue() : 0;
    }

    public void save() {
        String query = "INSERT INTO `measurements`(`Date`,`min`,`max`,`avg`) VALUES (?,?,?,?);";
        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dt.format(date));
            stmt.setInt(2, this.min);
            stmt.setInt(3, this.max);
            stmt.setInt(4, this.avg);

            stmt.executeUpdate();

            //Logger.createLogger().info("DB query: min %d, max %d, avg %d", this.min, this.max, this.avg);
            stmt.close();
            Measurement.clear();
        } catch (SQLException e) {
            Logger.createLogger().error(e.toString());
        }


    }
}
