package App.FileHandlers;

import App.Graphics.GraphicsBall;
import App.Graphics.GraphicsLine;
import App.Graphics.GraphicsShape;
import App.Simulation.Body.Body;
import App.Simulation.Util.Vec2;
import App.Util.SimulationOptions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SimulationFileReader {

  public SimulationFileReader(String filePath) throws IOException {
      mFile = new RandomAccessFile(filePath, "r");
      loadSimulationOptions();
      loadStaticBodies();
      loadDynamicBodies();
  }

  public boolean eof() throws IOException {
    return mFile.getFilePointer() >= mFile.length();
  }

  public List<Vec2> loadFrame() throws IOException {
    List<Vec2> positions = new ArrayList<>();

    byte[] buffer = new byte[mFrameSize];
    mFile.read(buffer);
    ByteArrayInputStream istream = new ByteArrayInputStream(buffer);

    while(istream.available() > 0) {
      double posx = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
      double posy = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
      positions.add(new Vec2(posx, posy));
    }

    return positions;
  }

  private SimulationOptions loadSimulationOptions() throws IOException {
    int screenWidth = mFile.readInt();
    int screenHeight = mFile.readInt();
    int FPS = mFile.readInt();
    mStaticBodiesSize = mFile.readInt();
    mDynamicBodiesSize = mFile.readInt();
    mFrameSize = mFile.readInt();
    return new SimulationOptions(screenWidth, screenHeight, FPS);
  }

  private List<GraphicsShape> loadStaticBodies() throws IOException {
    List<GraphicsShape> shapes = new ArrayList<>();

    byte[] buffer = new byte[mStaticBodiesSize];
    mFile.read(buffer);
    ByteArrayInputStream istream = new ByteArrayInputStream(buffer);

    while(istream.available() > 0) {
      int type = ByteBuffer.wrap(istream.readNBytes(Integer.SIZE / 8)).getInt(); 
      if(type == Body.Type.LINE.ordinal()) {
        double p1x = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
        double p1y = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
        double p2x = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
        double p2y = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
        double thickness = ByteBuffer.wrap(istream.readNBytes(Double.SIZE  / 8)).getDouble();
        shapes.add(new GraphicsLine(new Vec2(p1x, p1y), new Vec2(p2x, p2y), thickness));
      }
    }

    return shapes;
  }

  private List<GraphicsShape> loadDynamicBodies() throws IOException {
    List<GraphicsShape> shapes = new ArrayList<>();

    byte[] buffer = new byte[mDynamicBodiesSize];
    mFile.read(buffer);
    ByteArrayInputStream istream = new ByteArrayInputStream(buffer);

    while(istream.available() > 0) {
      int type = ByteBuffer.wrap(istream.readNBytes(Integer.SIZE / 8)).getInt(); 
      if(type == Body.Type.PARTICLE.ordinal()) {
        double px = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
        double py = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
        double radius = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
        shapes.add(new GraphicsBall(new Vec2(px, py), radius));
      }
    }

    return shapes;
  }

  private final RandomAccessFile mFile;

  private int mStaticBodiesSize;
  private int mDynamicBodiesSize;
  private int mFrameSize;
}
