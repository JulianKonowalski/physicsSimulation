package App.Simulation;

import App.Simulation.Body.Body;
import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import java.util.ArrayList;
import java.util.List;

public class SimulationState {

  public SimulationState(List<Line> walls, List<Particle> particles) {
    mWalls = walls;
    mParticles = particles;
  }

  public List<Line> getWalls() {
    return mWalls;
  }

  public List<Particle> getParticles() {
    return mParticles;
  }

  public List<Body> getBodies() {
    List<Body> bodies = new ArrayList<>();
    bodies.addAll(mWalls);
    bodies.addAll(mParticles);
    return bodies;
  }

  private final List<Line> mWalls;
  private final List<Particle> mParticles;
}