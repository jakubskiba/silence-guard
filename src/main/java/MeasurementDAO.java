import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MeasurementDAO {
    final SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
    Connection connection;


    public MeasurementDAO(Connection connection) {
        this.connection = connection;
    }

    public void save() {
        String query = "INSERT INTO `measurements`(`Date`,`min`,`max`,`avg`) VALUES (?,?,?,?);";
        FlatMeasurement flatMeasurement = new FlatMeasurement();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dt.format(flatMeasurement.date));
            stmt.setInt(2, flatMeasurement.min);
            stmt.setInt(3, flatMeasurement.max);
            stmt.setInt(4, flatMeasurement.avg);

            stmt.executeUpdate();

            //Logger.createLogger().info("DB query: min %d, max %d, avg %d", this.min, this.max, this.avg);
            stmt.close();
        } catch (SQLException e) {
            Logger.createLogger().error(e.toString());
        }


    }

    public List<FlatMeasurement> getSavedMeasurements(Date from, Date to) {
        List<FlatMeasurement> flatMeasurements = new ArrayList<>();

        String query = "SELECT * FROM measurements WHERE DATE BETWEEN ? AND ? AND avg != 0";
        ResultSet rs = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dt.format(from));
            stmt.setString(2, dt.format(to));

            rs = stmt.executeQuery();

            while(rs.next()) {
                flatMeasurements.add(createFlatMeasurement(rs));
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            Logger.createLogger().error(e.toString());
        }

        return flatMeasurements;
    }

    private FlatMeasurement createFlatMeasurement(ResultSet rs) throws SQLException {
        Integer min = rs.getInt("min");
        Integer max = rs.getInt("max");
        Integer avg = rs.getInt("avg");
        Date date = rs.getDate("Date");

        return new FlatMeasurement(min, max, avg, date);
    }
}
