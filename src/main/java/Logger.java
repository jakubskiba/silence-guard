import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Logger {

    enum LogType {
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }

    private static Logger logger = null;

    private Map<LogType, List<String>> logs;

    private Logger() {
        this.logs = new HashMap<>();
        for (LogType type : LogType.values()) {
            this.logs.put(type, new ArrayList<>());
        }
    }

    public static Logger createLogger() {
        if(logger == null) {
            Logger.logger = new Logger();
        }

        return Logger.logger;
    }

    public List<String> getAll() {
        List<String> allLog = this.logs.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return allLog.stream()
                .sorted(String::compareTo)
                .collect(Collectors.toList());
    }

    public List<String> getDebug() {
        return this.logs.get(LogType.DEBUG);
    }

    public List<String> getInfo() {
        return this.logs.get(LogType.INFO);
    }

    public List<String> getWarning() {
        return this.logs.get(LogType.WARNING);
    }

    public List<String> getError() {
        return this.logs.get(LogType.ERROR);
    }

    private String createLog(String msg, LogType type) {
        StringBuilder log = new StringBuilder();

        Date now = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss.SS");
        log.append(date.format(now)).append(" - ");
        log.append(time.format(now)).append(" - ");
        log.append(type.toString()).append(" - ");
        log.append(msg);

        return log.toString();
    }

    public void debug(String msg) {
        String log = createLog(msg, LogType.DEBUG);
        this.logs.get(LogType.DEBUG).add(log);
    }

    public void info(String msg) {
        String log = createLog(msg, LogType.INFO);
        this.logs.get(LogType.INFO).add(log);
        this.saveLogger();
    }

    public void error(String msg) {
        String log = createLog(msg, LogType.ERROR);
        this.logs.get(LogType.ERROR).add(log);
    }

    public void warning(String msg) {
        String log = createLog(msg, LogType.WARNING);
        this.logs.get(LogType.WARNING).add(log);

    }

    public void saveLogger() {
        try {
            String filename = String.format("%s.log", new Date().toString());
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            this.getAll().forEach(writer::println);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            this.error(e.toString());
        }

    }

}
