package App;

public class App {

    public App(String windowTitle, int windowWidth, int windowHeight) {
        mWindowTitle = windowTitle;
        mWindowWidth = windowWidth;
        mWindowHeight = windowHeight;
    }

    public void run() {
        mWindow = new Window();
        mPanel = mWindow.setup(mWindowTitle, mWindowWidth, mWindowHeight);
        mPanel.startSimulation();
    }

    private final String mWindowTitle;
    private final int mWindowWidth;
    private final int mWindowHeight;
    private Window mWindow;
    private SimulationPanel mPanel;
}
