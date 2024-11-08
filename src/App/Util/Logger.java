package App.Util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public Logger(String filePath, String dateFormat) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, true);
            mOut = new BufferedWriter(fileWriter);
            mFormatter = DateTimeFormatter.ofPattern(dateFormat);
            mIsActive = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            mIsActive = false;
        }
    }

    public boolean isActive() { return mIsActive; }

    public void setFilePath(String filePath) {
        try {
            mOut.close();
            mIsActive = false;
            FileWriter fileWriter = new FileWriter(filePath, true);
            mOut = new BufferedWriter(fileWriter);
            mIsActive = true;
        } catch (IOException e) {
            this.log("Logger", e.getMessage());
        }
    }

    public boolean log(String source, String message) {
        String log = makeLog(source + " | " + message);
        try {
            mOut.write(log, 0, log.length());
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            mOut.close();
            mIsActive = false;
        } catch (IOException e) {
            this.log("Logger", e.getMessage());
        }
    }

    private String makeLog(String message) {
        String timestamp = LocalDateTime.now().format(mFormatter);
        return timestamp + " | " + message + "\n";
    }

    Boolean mIsActive;
    BufferedWriter mOut;
    DateTimeFormatter mFormatter;
}
