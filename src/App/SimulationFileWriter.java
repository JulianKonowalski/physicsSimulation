package App;

import App.Simulation.Body.Body;
import App.Simulation.Body.Body.Type;
import App.Simulation.Body.DynamicBody;
import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.Body.StaticBody;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;


public class SimulationFileWriter {

  public SimulationFileWriter(String filePath) throws IOException {
    mPayloadSize = 0;
    mFilePath = filePath;
    mOut = new DataOutputStream(
      Files.newOutputStream(
        Path.of(mFilePath), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
      )
    );
  }

  public void writeHeader(int screenWidth, int screenHeight, int FPS) throws IOException {
    int sectionPayload = 0;
    sectionPayload += writeInt(screenWidth);
    sectionPayload += writeInt(screenHeight);
    sectionPayload += writeInt(FPS);
    mPayloadSize += sectionPayload;
  }

  public void writeStaticBodies(List<StaticBody> bodies) throws IOException {
    int sectionPayload = 0;
    sectionPayload += writeInt(Tags.STATIC_BODIES_START.ordinal());
    for (Body body : bodies) {
      switch (body.type()) {
        case Type.LINE -> { sectionPayload += writeLine((Line)body); }
        default -> {/* doNothing */}
      }
    }
    sectionPayload += writeInt(Tags.STATIC_BODIES_END.ordinal());
    mPayloadSize += sectionPayload;
  }

  public void writeDynamicBodies(List<DynamicBody> bodies) throws IOException {
    int sectionPayload = 0;
    sectionPayload += writeInt(Tags.DYNAMIC_BODIES_START.ordinal());
    for (Body body : bodies) {
      switch (body.type()) {
        case Type.PARTICLE -> { sectionPayload += writeParticle((Particle)body); }
        default -> {/* doNothing */}
      }
    }
    sectionPayload += writeInt(Tags.DYNAMIC_BODIES_END.ordinal());
    mPayloadSize += sectionPayload;
  }

  public void writeFrame(List<DynamicBody> bodies) throws IOException {
    int sectionPayload = 0;
    sectionPayload += writeInt(Tags.START_FRAME.ordinal());
    for(DynamicBody body : bodies) {
      sectionPayload += writeDouble(body.position().x());
      sectionPayload += writeDouble(body.position().y());
    }
    sectionPayload += writeInt(Tags.END_FRAME.ordinal());
    mPayloadSize += sectionPayload;
  }

  public void close() throws IOException { 
    mOut.close(); 
    cleanup();
  }

  private int writeInt(int data) throws IOException {
    mOut.writeInt(data);
    return Integer.SIZE / 8;
  }

  private int writeDouble(double data) throws IOException {
    mOut.writeDouble(data);
    return Double.SIZE / 8;
  }

  private int writeParticle(Particle particle) throws IOException {
    int sectionPayload = 0;
    sectionPayload += writeInt(Tags.PARTICLE.ordinal());
    sectionPayload += writeDouble(particle.position().x());
    sectionPayload += writeDouble(particle.position().y());
    sectionPayload += writeDouble(particle.radius());
    return sectionPayload;
  }

  private int writeLine(Line line) throws IOException {
    int sectionPayload = 0;
    sectionPayload += writeInt(Tags.LINE.ordinal());
    sectionPayload += writeDouble(line.p1().x());
    sectionPayload += writeDouble(line.p1().y());
    sectionPayload += writeDouble(line.p2().x());
    sectionPayload += writeDouble(line.p2().y());
    sectionPayload += writeDouble(line.thickness());
    return sectionPayload;
  }

  private void cleanup() throws IOException {
    try(
      RandomAccessFile file = new RandomAccessFile(mFilePath, "rw");
      FileChannel fileChannel = file.getChannel();
    ) {
      fileChannel.truncate(mPayloadSize);
      file.close();
    } catch (FileNotFoundException e) { //this will never happen, the file is created with the object
      System.out.println(e.getMessage());
      System.exit(0);
    }
  }

  private enum Tags {
    STATIC_BODIES_START,
    STATIC_BODIES_END,
    DYNAMIC_BODIES_START,
    DYNAMIC_BODIES_END,
    START_FRAME,
    END_FRAME,
    PARTICLE,
    LINE,
  }

  private int mPayloadSize;
  private final DataOutputStream mOut;
  private final String mFilePath;
}
