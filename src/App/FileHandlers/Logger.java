package App.FileHandlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

  public Logger(String filePath, String dateFormat) throws IOException {
    mOut = Files.newOutputStream(Path.of(filePath), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    mFormatter = DateTimeFormatter.ofPattern(dateFormat);
  }

  public void log(String source, String message) throws IOException {
    byte[] log = makeLog(source + " | " + message).getBytes();
    mOut.write(log);
  }

  public void close() throws IOException { mOut.close(); }

  private String makeLog(String message) {
    String timestamp = LocalDateTime.now().format(mFormatter);
    return timestamp + " | " + message + "\n";
  }

  OutputStream mOut;
  DateTimeFormatter mFormatter;
}
