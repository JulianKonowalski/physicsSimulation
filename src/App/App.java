package App;

import App.FileHandlers.Logger;
import App.FileHandlers.SimulationFileWriter;
import App.Simulation.Simulation;
import App.Util.Timer;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class App implements Runnable {

  public App(String windowTitle, int windowWidth, int windowHeight, int FPS, String logFilePath, String logDateFormat) {
    mTimestep = 1.0 / FPS;
    mWindow = new Window();
    mPanel = mWindow.setup(windowTitle, windowWidth, windowHeight, FPS);
    mTimer = new Timer();
    mSimulation = new Simulation(mTimestep);
    mSimulationThread = new Thread(this);
    openLogger(logFilePath, logDateFormat);
    openSimulationFileWriter("physicsSimulation.sim", windowWidth, windowHeight, FPS);
  }

  @Override
  public void run() {
    this.log("App", "Started the app");
    mTimer.start();

    while (mWindow.isActive()) { //MAIN LOOP
      mPanel.resolveMouseEvents();
      mSimulation.update();
      mPanel.drawScene(mSimulation.getState().getBodies());
      this.writeFrame();
      this.frameSync();
      mTimer.start();
    }

    this.log("App", "Closed the app\n");
    this.closeFileStreams();
  }

  private void openLogger(String logFilePath, String logDateFormat) {
    try {
      mLogger = new Logger(logFilePath, logDateFormat);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }
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
    try {
      mLogger.log(source, message);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(0);
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

  private void frameSync() { //delay a frame to match the target FPS
    try {
      long sleepTime = (long) (mTimestep * 1e9) - mTimer.getElapsedTime();
      if(sleepTime < 0) { throw new IllegalStateException("Simulation is running too slow"); }
      TimeUnit.NANOSECONDS.sleep(sleepTime);
    } catch (InterruptedException e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }
  }

  private void closeFileStreams() {
    try {
      mLogger.close();
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
  private final Thread mSimulationThread;
  
  private Logger mLogger;
  private SimulationFileWriter mFileWriter;
}
