package App.Util;

import java.lang.management.ManagementFactory;

public class AppOptions {
  public AppOptions(int screenWidth, int screenHeight, int FPS) {
    mScreenWidth = screenWidth;
    mScreenHeight = screenHeight;
    mFPS = FPS;
  }

  public int getWidth() { return mScreenWidth; }
  public int getHeight() { return mScreenHeight; }
  public int getFPS() { return mFPS; }
  public boolean isDebugging() { return isDebugging; }

  private final int mScreenWidth;
  private final int mScreenHeight;
  private final int mFPS;
  private final boolean isDebugging = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
}
