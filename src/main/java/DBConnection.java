import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection = null;

    public static Connection getConnection(String database_path) {
        String database = String.format("jdbc:sqlite:%s", database_path);
        if(connection == null) {
            try {
                connection = DriverManager.getConnection(database);
            } catch (SQLException e) {
                DBConnection.printSQLException(e);
            }

        }
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            Logger.createLogger().error(e.toString());
        }
    }

    public static void printSQLException(SQLException e) {
        System.err.println("Database failure: " + e.getMessage());
    }
}
