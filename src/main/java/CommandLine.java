import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

class CommandLine extends Thread {
    static String help_msg = "command | description\n" +
            "?       | shows this help\n" +
            "l       | show log\n" +
            "x       | exits program" +
            "t       | sets threshold value\n";

    public void run() {
        Scanner in = new Scanner(System.in);
        while(!App.stopCapture) {
            System.out.print("> ");
            String command = in.nextLine();
            String[] splitted = command.split(" ");
            switch (splitted[0]) {
                case "t":
                    try {
                        Integer newValue = Integer.parseInt(splitted[1]);

                        changeThreshold(newValue);
                    } catch (Exception e) {
                        Arrays.asList(splitted).forEach(System.out::println);
                        Logger.createLogger().error(e.toString());
                    }
                    break;

                case "l":
                    showLogger();
                    break;

                case "x":
                    exit();
                    break;

                case "?":
                case "help":
                    System.out.println(help_msg);
                    break;

                default:
                    System.out.println("Wrong command!");
                    break;
            }
        }
    }

    public void changeThreshold(Integer newValue) {
        App.thresholdValue = newValue;
        System.out.println("Threshold set to: " + newValue);
        String logMessage = String.format("Threshold was changed to: %d", newValue);
        Logger.createLogger().info(logMessage);
    }

    public void showLogger() {
        Logger.createLogger().getAll().forEach(System.out::println);
    }

    public void exit() {
        Logger.createLogger().info("System halted");
        Logger.createLogger().saveLogger();
        showLogger();
        App.stopCapture = true;
        App.mainThread.interrupt();
    }
}