package App;

import App.Simulation.Simulation;
import App.Util.Logger;
import App.Util.Timer;
import java.util.concurrent.TimeUnit;

public class App implements Runnable {

  public App(String windowTitle, int windowWidth, int windowHeight, int FPS, String logFilePath, String logDateFormat) {
    mTimestep = 1.0 / FPS;
    mWindow = new Window();
    mPanel = mWindow.setup(windowTitle, windowWidth, windowHeight, FPS);
    mLogger = new Logger(logFilePath, logDateFormat);
    mTimer = new Timer();
    mSimulation = new Simulation(mTimestep);
    mSimulationThread = new Thread(this);
  }

  @Override
  public void run() {
    mLogger.log("App", "Started the app");
    mTimer.start();

    while (mSimulationThread != null) { //MAIN LOOP
      mPanel.resolveMouseEvents();
      mSimulation.update();
      mPanel.drawScene(mSimulation.getState().getBodies());
      this.frameSync();
      mTimer.start();
    }

    mLogger.log("App", "Closed the app\n");
    mLogger.close();
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
  private final Logger mLogger;
  private final Timer mTimer;
  private final Simulation mSimulation;
  private final Thread mSimulationThread;
}
