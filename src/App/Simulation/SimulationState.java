package App.Simulation;

import App.Simulation.Body.*;

import java.util.ArrayList;
import java.util.List;

public class SimulationState {

  public SimulationState(List<StaticBody> walls, List<DynamicBody> particles) {
    mStaticBodies = walls;
    mDynamicBodies = particles;
  }

  public List<StaticBody> getStaticBodies() { return mStaticBodies; }
  public List<DynamicBody> getDynamicBodies() { return mDynamicBodies; }

  public List<Body> getBodies() {
    List<Body> bodies = new ArrayList<>();
    bodies.addAll(mDynamicBodies);
    bodies.addAll(mStaticBodies);
    return bodies;
  }

  private final List<StaticBody> mStaticBodies;
  private final List<DynamicBody> mDynamicBodies;
}