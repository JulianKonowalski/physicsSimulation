package App;

import App.FileHandlers.*;
import App.Simulation.Simulation;
import App.Util.Pair;
import App.Util.Timer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class App implements Runnable {

  public App(String windowTitle, int windowWidth, int windowHeight, int FPS, String logFilePath, String logDateFormat) {
    mTimestep = 1.0 / FPS;
    mWindow = new Window();
    mPanel = mWindow.setup(windowTitle, windowWidth, windowHeight, FPS);
    mTimer = new Timer();
    openLogger(logFilePath, logDateFormat);
    mSimulation = new Simulation(mTimestep, (String source, String message) -> { this.log(source, message); });
    openSimulationFileWriter("physicsSimulation.sim", windowWidth, windowHeight, FPS);
  }

  @Override
  public void run() {
    this.log("App", "Started the app");
    mTimer.start();

    while (mWindow.isDisplayable()) { //MAIN LOOP
      mPanel.resolveMouseEvents();
      mPanel.drawScene(mSimulation.getState().getBodies());
      this.writeFrame();
      mSimulation.update();
      this.frameSync();
      mTimer.start();
    }

    this.log("App", "Closed the app\n");
    this.closeFileStreams();
  }

  private void frameSync() { //delay a frame to match the target FPS
    try {
      long sleepTime = (long) Math.max(0, mTimestep * 1e9 - mTimer.getElapsedTime()) ;
//      if(sleepTime < 0) { throw new IllegalStateException("Simulation is running too slow"); }
      TimeUnit.NANOSECONDS.sleep(sleepTime);
    } catch (InterruptedException e) {
      System.out.println(e.getMessage());
      System.exit(-1);
    }
  }

  private void openLogger(String logFilePath, String logDateFormat) {
    sLogs = new ArrayList<>();
    sLoggerLock = new ReentrantLock();
    sLoggerCondition = sLoggerLock.newCondition(); 
    try {
      mLogger = new Logger(logFilePath, logDateFormat, sLogs, sLoggerLock, sLoggerCondition);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }
    Thread loggerThread = new Thread(mLogger);
    loggerThread.start();
  }

  private void openSimulationFileWriter(String filePath, int windowWidth, int windowHeight, int FPS) {
    try {
      mFileWriter = new SimulationFileWriter(filePath);
      mFileWriter.writeHeader(windowWidth, windowHeight, FPS);
      mFileWriter.writeStaticBodies(mSimulation.getState().getStaticBodies());
      mFileWriter.writeDynamicBodies(mSimulation.getState().getDynamicBodies());
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }
  }

  private void log(String source, String message) {
    sLoggerLock.lock();
    try {
      sLogs.add(new Pair<>(source, message));
      sLoggerCondition.signalAll();   
    } finally {
      sLoggerLock.unlock();
    }   
  }

  private void writeFrame() {
    try {
      mFileWriter.writeFrame(mSimulation.getState().getDynamicBodies());
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }
  }

  private void closeLogger() {
    sLoggerLock.lock();
    try {
      mLogger.close();
      sLoggerCondition.signalAll();
    } finally {
      sLoggerLock.unlock();
    }
  }

  private void closeFileStreams() {
    closeLogger();
    try {
      mFileWriter.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }
  }

  private final double mTimestep;
  private final Window mWindow;
  private final SimulationPanel mPanel;
  private final Timer mTimer;
  private final Simulation mSimulation;
  
  private static List<Pair<String, String>> sLogs;
  private static Lock sLoggerLock;
  private static Condition sLoggerCondition;
  private Logger mLogger;

  private SimulationFileWriter mFileWriter;
}