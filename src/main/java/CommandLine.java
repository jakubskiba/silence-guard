import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

class CommandLine extends Thread {
    public void run() {
        Scanner in = new Scanner(System.in);
        while(!App.stopCapture) {
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
                    Logger.createLogger().info("System halted");
                    Logger.createLogger().saveLogger();
                    App.stopCapture = true;
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
}