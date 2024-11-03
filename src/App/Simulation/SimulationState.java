package App.Simulation;

import App.Simulation.Body.Body;
import java.util.ArrayList;
import java.util.List;

public class SimulationState {

  public SimulationState() { mBodies = new ArrayList<>(); }
  public SimulationState(List<Body> bodies) { mBodies = bodies; }

  public void addBody(Body body) { mBodies.add(body); }
  public List<Body> getBodies() { return mBodies; }

  private final List<Body> mBodies;
}