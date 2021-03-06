import java.sql.Connection;

public class App {

    static Integer thresholdValue = 80;
    static Integer beeperDuration = 5000;
    static Integer saveDataInterval = 30000;
    static boolean stopCapture = false;
    static boolean isBeeperOn = false;
    static Thread mainThread;
    public static void main(String[] args) {
        mainThread = Thread.currentThread();
        Logger.createLogger().info("System started");

        Connection connection = DBConnection.getConnection("measurements.db");

        CommandLine commandLine = new CommandLine();
        SilenceGuard silenceGuard = new SilenceGuard();
        System.out.println("Welcome to silence guard");
        App.printProgramStatus();
        System.out.println(CommandLine.help_msg);

        silenceGuard.start();
        commandLine.start();

        while(!App.stopCapture) {
            try {
                Logger.createLogger().saveLogger();
                new MeasurementDAO(connection).save();
                Measurement.clear();
                Thread.sleep(saveDataInterval);
            } catch (InterruptedException e) {
                Logger.createLogger().error(e.toString());
            }
        }

        DBConnection.closeConnection();
    }

    public static void printProgramStatus() {
        System.out.println(String.format("threshold value: %d", thresholdValue));
        String beeperStatus = isBeeperOn ? "on" : "off";
        System.out.println("Beeper: " + beeperStatus);
    }


}
