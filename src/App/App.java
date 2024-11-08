package App;

public class App {

    public App(String windowTitle, int windowWidth, int windowHeight, int FPS) {
        mWindowTitle = windowTitle;
        mWindowWidth = windowWidth;
        mWindowHeight = windowHeight;
        mFPS = FPS;
    }

    public void run() {
        mWindow = new Window();
        mPanel = mWindow.setup(mWindowTitle, mWindowWidth, mWindowHeight, mFPS);
        mPanel.startSimulation();
    }

    private final String mWindowTitle;
    private final int mWindowWidth;
    private final int mWindowHeight;
    private final int mFPS;
    private Window mWindow;
    private SimulationPanel mPanel;
}
