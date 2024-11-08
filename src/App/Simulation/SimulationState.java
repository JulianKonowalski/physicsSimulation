package App.Simulation;

import App.Simulation.Body.NewBody;

import java.util.ArrayList;
import java.util.List;

public class SimulationState {

  public SimulationState() { mBodies = new ArrayList<>(); }
  public SimulationState(List<NewBody> bodies) { mBodies = bodies; }

  public void addBody(NewBody body) { mBodies.add(body); }
  public List<NewBody> getBodies() { return mBodies; }

  private final List<NewBody> mBodies;
}