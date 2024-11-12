package App.Util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

  public Logger(String filePath, String dateFormat) throws IOException {
    FileWriter fileWriter = new FileWriter(filePath, true);
    mOut = new BufferedWriter(fileWriter);
    mFormatter = DateTimeFormatter.ofPattern(dateFormat);
  }

  public void setFilePath(String filePath) throws IOException {
    mOut.close();
    FileWriter fileWriter = new FileWriter(filePath, true);
    mOut = new BufferedWriter(fileWriter);
  }

  public boolean log(String source, String message) throws IOException {
    String log = makeLog(source + " | " + message);
    mOut.write(log, 0, log.length());
    return true;
  }

  public void close() throws IOException { mOut.close(); }

  private String makeLog(String message) {
    String timestamp = LocalDateTime.now().format(mFormatter);
    return timestamp + " | " + message + "\n";
  }

  BufferedWriter mOut;
  DateTimeFormatter mFormatter;
}
