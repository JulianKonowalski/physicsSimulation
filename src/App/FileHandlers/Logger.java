package App.FileHandlers;

import App.Util.Pair;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Logger implements Runnable {

  public Logger(String filePath, String dateFormat, List<Pair<String, String>> logs, Lock lock, Condition condition) throws IOException {
    mIsActive = true;
    mLogs = logs;
    mLock = lock;
    mCondition = condition;
    mOut = Files.newOutputStream(Path.of(filePath), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    mFormatter = DateTimeFormatter.ofPattern(dateFormat);
  }

  @Override
  public void run() {
    while (mIsActive) {
      mainLoop();
    }
  }

  public void close() { mIsActive = false; }

  private void mainLoop() {
    mLock.lock();
    try { mCondition.await(); } 
    catch (InterruptedException e) { System.out.println(e.getMessage()); }
    finally {
      if(!mIsActive) { shutdown(); }
      else {
        writeLogs();
        mLogs.clear();
        mLock.unlock();
      }
    }
  }

  private void writeLogs() {
    for(Pair<String, String> logMsg : mLogs) {
      log(logMsg.first(), logMsg.second());
    }
  }

  private void log(String source, String message) {
    try {
      byte[] log = makeLog(source + " | " + message).getBytes();
      mOut.write(log);
    } catch (IOException e) { System.out.println(e.getMessage()); }
  }

  private String makeLog(String message) {
    String timestamp = LocalDateTime.now().format(mFormatter);
    return timestamp + " | " + message + "\n";
  }

  private void shutdown() {
    try { 
      mOut.close(); 
    } 
    catch (IOException e) { 
      System.out.println(e.getMessage()); 
      System.exit(0); 
    }
  }

  OutputStream mOut;
  DateTimeFormatter mFormatter;

  private static Lock mLock;
  private static Condition mCondition;
  private static List<Pair<String, String>> mLogs;
  private static boolean mIsActive;
}