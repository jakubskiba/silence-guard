public class App {

    static Integer thresholdValue = 200;
    static Integer beeperDuration = 5000;
    static boolean stopCapture = false;
    static boolean isBeeperOn = false;
    static Thread mainThread;
    public static void main(String[] args) {
        mainThread = Thread.currentThread();
        Logger.createLogger().info("System started");

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
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                Logger.createLogger().error(e.toString());
            }
        }
    }

    public static void printProgramStatus() {
        System.out.println(String.format("threshold value: %d", thresholdValue));
        String beeperStatus = isBeeperOn ? "on" : "off";
        System.out.println("Beeper: " + beeperStatus);
    }
}
