package App;

import App.FileHandlers.*;
import App.Simulation.Simulation;
import App.Simulation.SimulationState;
import App.Util.Pair;
import App.Util.Timer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.management.ManagementFactory;

public class App implements Runnable {

  public App(String windowTitle, int width, int height, int FPS, String logFilePath, String logDateFormat) {
    mTimestep = 1.0 / FPS;
    mWidth = width;
    mHeight = height;
    mTimer = new Timer();
    openLogger(logFilePath, logDateFormat);

    InitializationInterface init = new InitializationInterface(width, height);
    SimulationState initialState = init.getInitialState();
    mSimulation = new Simulation(initialState, mTimestep, this::log);
    openSimulationFileWriter(width, height, FPS);

    mWindow = new Window();
    mPanel = mWindow.setup(windowTitle, width, height, FPS);
  }

  @Override
  public void run() {
    this.log("App", "Started the app");

    mTimer.start();
    while (mWindow.isDisplayable()) { //MAIN LOOP
      mPanel.resolveMouseEvents();
      mPanel.drawScene(mSimulation.getState().Bodies());
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
      long sleepTime = (long) (mTimestep * 1e9 - mTimer.getElapsedTime());
      if(isDebugging){
        sleepTime = Math.max(0, sleepTime);
        TimeUnit.NANOSECONDS.sleep(sleepTime);
        return;
      }
      if(sleepTime < 0) { throw new IllegalStateException("Simulation is running too slow"); }
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

  private void openSimulationFileWriter(int windowWidth, int windowHeight, int FPS) {
    try {
      mFileWriter = new SimulationFileWriter("physicsSimulation.sim");
      mFileWriter.writeHeader(windowWidth, windowHeight, FPS);
      mFileWriter.writeStaticBodies(mSimulation.getState().StaticBodies());
      mFileWriter.writeDynamicBodies(mSimulation.getState().DynamicBodies());
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
      mFileWriter.writeFrame(mSimulation.getState().DynamicBodies());
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(-1);
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
  private final int mWidth;
  private final int mHeight;
  private final Simulation mSimulation;
  private final Timer mTimer;
  private final Window mWindow;
  private final SimulationPanel mPanel;

  private static List<Pair<String, String>> sLogs;
  private static Lock sLoggerLock;
  private static Condition sLoggerCondition;
  private Logger mLogger;
  private SimulationFileWriter mFileWriter;

   public boolean isDebugging = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
}