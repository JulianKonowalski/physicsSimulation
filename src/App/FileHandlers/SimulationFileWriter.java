package App.FileHandlers;

import App.Simulation.Body.Body;
import App.Simulation.Body.Body.Type;
import App.Simulation.Body.DynamicBody;
import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.Body.StaticBody;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/*
 * BRIEF
 * This class saves the whole simulation into a .sim file
 * 
 * FILE STRUCTURE
 * File begins with a header containing:
 * -screenWidth
 * -screenHeight
 * -FPS
 * -staticBodies section size
 * -dynamicBodies section size
 * -frame data section size
 * 
 * staticBodies section contains all static bodies listed one after another
 * with their types and positions
 * 
 * dynamicBodies section contains all dynamic bodies listed one after
 * another with their types, positions
 * 
 * frame data section contains the positions of all of the dynamicBody
 * objects in a specific frame. Each frame has the same size, so the 
 * header saves the size of one frame, not the whole data payload.
 * Each objects' data is always kept in the same place within the frame
 * data structure
 */

public class SimulationFileWriter {

  public SimulationFileWriter(String filePath) throws IOException {
    mPayloadSize = 0;
    mFrameSize = null;
    mFile = new RandomAccessFile(filePath, "rw");
  }

  public void writeHeader(int screenWidth, int screenHeight, int FPS) throws IOException {
    int sectionPayload = 0;
    sectionPayload += writeInt(screenWidth);
    sectionPayload += writeInt(screenHeight);
    sectionPayload += writeInt(FPS);
    mPlaceholderDataPosition = mFile.getFilePointer();
    for(int i = 0; i < 3; ++i) { sectionPayload += writeInt(0); } //placeholder data
    mPayloadSize += sectionPayload;
  }

  public void writeStaticBodies(List<StaticBody> bodies) throws IOException {
    int sectionPayload = 0;
    for (Body body : bodies) {
      switch (body.type()) {
        case Type.LINE -> { sectionPayload += writeLine((Line)body); }
        default -> {/* doNothing */}
      }
    }
    mStaticBodiesSize = sectionPayload;
    mPayloadSize += sectionPayload;
  }

  public void writeDynamicBodies(List<DynamicBody> bodies) throws IOException {
    int sectionPayload = 0;
    for (Body body : bodies) {
      switch (body.type()) {
        case Type.PARTICLE -> { sectionPayload += writeParticle((Particle)body); }
        default -> {/* doNothing */}
      }
    }
    mDynamicBodiesSize = sectionPayload;
    mPayloadSize += sectionPayload;
  }

  public void writeFrame(List<DynamicBody> bodies) throws IOException {
    int sectionPayload = 0;
    for(DynamicBody body : bodies) {
      sectionPayload += writeDouble(body.position().x());
      sectionPayload += writeDouble(body.position().y());
    }
    if (mFrameSize == null) { mFrameSize = sectionPayload; }
    mPayloadSize += sectionPayload;
  }
  
  @SuppressWarnings("ConvertToTryWithResources")
  public void close() throws IOException {
    try {
      writeSectionData();
      mFile.getChannel().truncate(mPayloadSize); //remove any incomplete data that might have been written to the file
      mFile.close();
    } catch (FileNotFoundException e) { //this will never happen, the file is created with the object
      System.out.println(e.getMessage());
      System.exit(0);
    }
  }

  private int writeInt(int data) throws IOException {
    mFile.writeInt(data);
    return Integer.SIZE / 8;
  }

  private int writeDouble(double data) throws IOException {
    mFile.writeDouble(data);
    return Double.SIZE / 8;
  }

  private int writeParticle(Particle particle) throws IOException {
    int sectionPayload = 0;
    sectionPayload += writeInt(Body.Type.PARTICLE.ordinal());
    sectionPayload += writeDouble(particle.position().x());
    sectionPayload += writeDouble(particle.position().y());
    sectionPayload += writeDouble(particle.radius());
    return sectionPayload;
  }

  private int writeLine(Line line) throws IOException {
    int sectionPayload = 0;
    sectionPayload += writeInt(Body.Type.LINE.ordinal());
    sectionPayload += writeDouble(line.p1().x());
    sectionPayload += writeDouble(line.p1().y());
    sectionPayload += writeDouble(line.p2().x());
    sectionPayload += writeDouble(line.p2().y());
    sectionPayload += writeDouble(line.thickness());
    return sectionPayload;
  }

  private void writeSectionData() throws IOException {
    mFile.seek(mPlaceholderDataPosition);
    mFile.writeInt(mStaticBodiesSize);
    mFile.writeInt(mDynamicBodiesSize);
    mFile.writeInt(mFrameSize);
  }

  private final RandomAccessFile mFile;
  private int mPayloadSize;
  private long mPlaceholderDataPosition;
  private int mStaticBodiesSize;
  private int mDynamicBodiesSize;
  private Integer mFrameSize;
}
