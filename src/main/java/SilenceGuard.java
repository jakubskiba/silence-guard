import javax.sound.sampled.*;
import java.io.*;


class SilenceGuard extends Thread {
    ByteArrayOutputStream byteArrayOutputStream;
    TargetDataLine targetDataLine;
    int cnt;
    Integer delta;

    byte tempBuffer[] = new byte[8000];
    int countzero, countdownTimer;
    short convert[] = new short[tempBuffer.length];


    public void run() {
        try {
            this.byteArrayOutputStream = new ByteArrayOutputStream();
            this.countdownTimer = 0;

            while (!App.stopCapture) {
                Integer silenceAmount = this.measureSound();
                if (silenceAmount <= App.thresholdValue) {
                    String logMessage = String.format("Exceeded noise level. Silence amount: %d. Threshold: %d", silenceAmount, App.thresholdValue);
                    Logger.createLogger().info(logMessage);
                    if(App.isBeeperOn) this.playSilencingSound("beep.wav");
                }

            }
        } catch (Exception e) {
            Logger.createLogger().error(e.getMessage());
        }
    }

    public Integer measureSound() throws LineUnavailableException, InterruptedException {
        AudioFormat audioFormat = new AudioFormat(8000.0F, 16, 1, true, false);
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        targetDataLine.open(audioFormat);
        targetDataLine.start();
        cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
        byteArrayOutputStream.write(tempBuffer, 0, cnt);
        try {
            countzero = 0;
            for (int i = 0; i < tempBuffer.length; i++) {
                convert[i] = tempBuffer[i];
                if (convert[i] == 0) {
                    countzero++;
                }
            }

            countdownTimer++;

        } catch (StringIndexOutOfBoundsException e) {
            Logger.createLogger().error(e.getMessage());
        }
        Thread.sleep(0);
        targetDataLine.close();

        return countzero;
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

