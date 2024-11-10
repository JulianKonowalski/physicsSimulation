package App.Simulation;

import App.Simulation.Body.NewBody;
import App.Simulation.Body.NewLine;
import App.Simulation.Body.NewParticle;

import java.util.ArrayList;
import java.util.List;

public class SimulationState {

  public SimulationState(List<NewLine> walls, List<NewParticle> particles) {
    mWalls = walls;
    mParticles = particles;
  }

  public List<NewLine> getWalls() {
    return mWalls;
  }

  public List<NewParticle> getParticles() {
    return mParticles;
  }

  public List<NewBody> getBodies() {
    List<NewBody> bodies = new ArrayList<>();
    bodies.addAll(mWalls);
    bodies.addAll(mParticles);
    return bodies;
  }

  private final List<NewLine> mWalls;
  private final List<NewParticle> mParticles;
}