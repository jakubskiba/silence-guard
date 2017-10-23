import javax.sound.sampled.*;
import javax.swing.text.StyledEditorKit;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


class SilenceGuard extends Thread {
    ByteArrayOutputStream byteArrayOutputStream;
    TargetDataLine targetDataLine;
    int cnt;
    Integer previousMeasurement = 0;


    byte tempBuffer[] = new byte[8000];
    int countzero, countdownTimer;
    short convert[] = new short[tempBuffer.length];


    public void run() {
        try {


            while (!App.stopCapture) {
                Process p = Runtime.getRuntime().exec("python3 sm.py");
                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(p.getInputStream()));

                List<Integer> measurements = stdInput.lines()
                                        .mapToDouble(m -> Double.parseDouble(m))
                                        .boxed()
                                        .mapToInt(m -> m.intValue())
                                        .boxed()
                                        .collect(Collectors.toList());

                measurements.forEach(m -> new Measurement(m, new Date()));
                Boolean isNoiseLevelExceeded = measurements.stream()
                                                            .filter(m -> m >= App.thresholdValue)
                                                            .findAny().isPresent();
                if (isNoiseLevelExceeded) {
                    Logger.createLogger().info("Noise level exceeded");
                    if(App.isBeeperOn) this.playSilencingSound("beep.wav");
                }

            }
        } catch (Exception e) {
            Logger.createLogger().error(e.getMessage());
        }
    }


    public static void playSilencingSound(String url){
        File soundFile = new File(url);
        try {
            AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
            DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(sound);
            clip.start();
            Thread.sleep(App.beeperDuration);
            clip.close();
        } catch (Exception e) {
           Logger.createLogger().error(e.getMessage());
        }

    }

}

