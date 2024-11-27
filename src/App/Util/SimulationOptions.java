package App.Util;

public class SimulationOptions {
  public SimulationOptions(int screenWidth, int screenHeight, int FPS) {
    mScreenWidth = screenWidth;
    mScreenHeight = screenHeight;
    mFPS = FPS;
  }

  public int getWidth() { return mScreenWidth; }
  public int getHeight() { return mScreenHeight; }
  public int getFPS() { return mFPS; }

  private final int mScreenWidth;
  private final int mScreenHeight;
  private final int mFPS;
}
