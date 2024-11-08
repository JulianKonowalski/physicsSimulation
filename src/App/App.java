package App;

import App.Util.Logger;

public class App {

    public App(String windowTitle, int windowWidth, int windowHeight, int FPS, String logFilePath, String logDateFormat) {
        mWindow = new Window();
        mPanel = mWindow.setup(windowTitle, windowWidth, windowHeight, FPS);
        mLogger = new Logger(logFilePath, logDateFormat);
    }

    public void run() {
        mLogger.log("App", "Started the app");
        mPanel.startSimulation();
        mLogger.close();
    }

    private final Window mWindow;
    private final SimulationPanel mPanel;
    private final Logger mLogger;
}
