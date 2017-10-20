import java.text.ParseException;

public class App {

    static Integer thresholdValue = 200;
    static Integer beeperDuration = 5000;
    static boolean stopCapture = false;

    public static void main(String[] args) {
        Logger.createLogger().info("System started");

        CommandLine commandLine = new CommandLine();
        SilenceGuard silenceGuard = new SilenceGuard();

        silenceGuard.start();
        commandLine.start();

        while(!App.stopCapture) {
            try {
                Logger.createLogger().saveLogger();
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                Logger.createLogger().error(e.toString());
            }
        }
    }

}
