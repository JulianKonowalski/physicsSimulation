package App.FileHandlers;

import App.Simulation.Body.Body;
import App.Simulation.Body.DynamicBody;
import App.Simulation.Body.StaticBody;
import App.Simulation.Util.Vec2;
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

  private void loadSimulationOptions() throws IOException { //TODO: create a data container for simulation options
    int screenWidth = mFile.readInt();
    int screenHeight = mFile.readInt();
    int FPS = mFile.readInt();
    mStaticBodiesSize = mFile.readInt();
    mDynamicBodiesSize = mFile.readInt();
    mFrameSize = mFile.readInt();
  }

  private List<StaticBody> loadStaticBodies() throws IOException { //TODO: change return type to list of shapes
    List<StaticBody>staticBodies = new ArrayList<>();

    byte[] buffer = new byte[mStaticBodiesSize];
    mFile.read(buffer);
    ByteArrayInputStream istream = new ByteArrayInputStream(buffer);

    while(istream.available() > 0) {
      int type = ByteBuffer.wrap(istream.readNBytes(Integer.SIZE / 8)).getInt(); 
      if(type == Body.Type.LINE.ordinal()) { //TODO: review this, it doesn't work with switch-case...
        double p1x = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
        double p1y = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
        double p2x = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
        double p2y = ByteBuffer.wrap(istream.readNBytes(Double.SIZE / 8)).getDouble();
        double thickness = ByteBuffer.wrap(istream.readNBytes(Double.SIZE  / 8)).getDouble();
      }
    }

    return staticBodies;
  }

  private List<DynamicBody> loadDynamicBodies() throws IOException { //TODO: change return type to list of shapes
    List<DynamicBody>dynamicBodies = new ArrayList<>();

    byte[] buffer = new byte[mDynamicBodiesSize];
    mFile.read(buffer);
    ByteArrayInputStream istream = new ByteArrayInputStream(buffer);

    while(istream.available() > 0) {
      int type = ByteBuffer.wrap(istream.readNBytes(Integer.SIZE / 8)).getInt(); 
      if(type == Body.Type.PARTICLE.ordinal()) { //TODO: review this, it doesn't work with switch-case...

      }
    }

    return dynamicBodies;
  }

  private final RandomAccessFile mFile;

  private int mStaticBodiesSize;
  private int mDynamicBodiesSize;
  private int mFrameSize;
}
