package App;

import App.FileHandlers.*;
import App.Graphics.AnimationConverter;
import App.Graphics.AnimationState;
import App.Simulation.Simulation;
import App.Simulation.SimulationState;
import App.Util.AppOptions;
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

  public App(String windowTitle, int width, int height, int FPS, String logFilePath, String logDateFormat) {
    sOptions = new AppOptions(width, height, FPS); //it should be initialized before any other object
    mTimestep = 1.0 / FPS;
    mTimer = new Timer();
    openLogger(logFilePath, logDateFormat);

    InitializationInterface init = new InitializationInterface();
    SimulationState initialState = init.getInitialState();
    mSimulation = new Simulation(initialState, mTimestep, this::log);
    openSimulationFileWriter(width, height, FPS);

    mAnimationConverter = new AnimationConverter();
    mAnimationState = new AnimationState(
      mAnimationConverter.getStaticShapes(mSimulation.getState()), 
      mAnimationConverter.getDynamicShapes(mSimulation.getState())
    );

    mWindow = new Window();
    mPanel = mWindow.setup(windowTitle);
  }

  @Override
  public void run() {
    this.log("App", "Started the app");

    mTimer.start();
    while (mWindow.isDisplayable()) { //MAIN LOOP
      mPanel.resolveMouseEvents();
      mPanel.drawScene(mAnimationState);
      this.writeFrame();
      mSimulation.update();
      mAnimationConverter.updateAnimationState(mAnimationState, mSimulation.getState());
      this.frameSync();
      mTimer.start();
    }

    this.log("App", "Closed the app\n");
    this.closeFileStreams();
  }

  private void frameSync() { //delay a frame to match the target FPS
    try {
      long sleepTime = (long) (mTimestep * 1e9 - mTimer.getElapsedTime());
      if(sOptions.isDebugging()) {
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

  public static AppOptions sOptions;

  private final Timer mTimer;
  private final double mTimestep;
  private final Simulation mSimulation;
  private final AnimationState mAnimationState;
  private final AnimationConverter mAnimationConverter;
  private final Window mWindow;
  private final SimulationPanel mPanel;

  private static List<Pair<String, String>> sLogs;
  private static Lock sLoggerLock;
  private static Condition sLoggerCondition;
  private Logger mLogger;
  private SimulationFileWriter mFileWriter;

}