package App;

import App.Simulation.Simulation;
import App.Util.Logger;
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

    try {//TODO: offload this to a specialised method
      mFileWriter = new SimulationFileWriter("physicsSimulation.sim");
      mLogger = new Logger(logFilePath, logDateFormat);
      mFileWriter.writeHeader(windowWidth, windowHeight, FPS);
      mFileWriter.writeStaticBodies(mSimulation.getState().getStaticBodies());
      mFileWriter.writeDynamicBodies(mSimulation.getState().getDynamicBodies());
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }
  }

  @Override
  public void run() {
    try {//TODO: offload this to a specialised method
      mLogger.log("App", "Started the app");
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }
    mTimer.start();

    while (mSimulationThread != null) { //MAIN LOOP
      mPanel.resolveMouseEvents();
      mSimulation.update();
      mPanel.drawScene(mSimulation.getState().getBodies());

      try {//TODO: offload this to a specialised method
          mFileWriter.writeFrame(mSimulation.getState().getDynamicBodies());
      } catch (IOException e) {
        System.out.println(e.getMessage());
        System.exit(0);
      }

      this.frameSync();
      mTimer.start();
    }

    try {//TODO: offload this to a specialised method
      mLogger.log("App", "Closed the app\n");
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }

    try {//TODO: offload this to a specialised method
      mLogger.close();
      mFileWriter.close();
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

  private final double mTimestep;
  private final Window mWindow;
  private final SimulationPanel mPanel;
  private final Timer mTimer;
  private final Simulation mSimulation;
  private final Thread mSimulationThread;
  
  private Logger mLogger;
  private SimulationFileWriter mFileWriter;
}
