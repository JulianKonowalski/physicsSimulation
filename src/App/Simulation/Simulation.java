package App.Simulation;

import App.Simulation.Body.*;
import App.Simulation.Util.FutureCollisionData;
import App.Simulation.Util.Pair;
import App.Simulation.Util.Vec2;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Simulation {

  public Simulation(double timestep) {
    mTimestep = timestep;
    mCollisionSolver = new CollisionSolver();
    mCollisionDetector = new CollisionDetector(mTimestep);
    List<StaticBody> walls = new ArrayList<>();
    List<DynamicBody> particles = new ArrayList<>();

//    particles.add(new Particle(new Vec2(100, 100), new Vec2(-100, -100), 5.0, 8));

    particles.add(new Particle(new Vec2(100, 100), new Vec2(100, 200), 5.0, 10));
    particles.add(new Particle(new Vec2(200, 200), new Vec2(-100, 200), 5.0, 10));
    particles.add(new Particle(new Vec2(300, 300), new Vec2(200, 100), 5.0, 10));
    particles.add(new Particle(new Vec2(400, 400), new Vec2(-200, -100), 5.0, 10));

    //particles.add(new Particle(new Vec2(320, 400), new Vec2(50, 0), 5.0, 10));

    walls.add(new Line(new Vec2(0, 0), new Vec2(1280, 0), 2.0));
    walls.add(new Line(new Vec2(1280, 0), new Vec2(1280, 720), 2.0));
    walls.add(new Line(new Vec2(1280, 720), new Vec2(0, 720), 2.0));
    walls.add(new Line(new Vec2(0, 720), new Vec2(0, 0), 2.0));

//    walls.add(new Line(new Vec2(0, 0), new Vec2(720, 720), 2.0));

    mState = new SimulationState(walls, particles);
  }

  public SimulationState getState() { return mState; }

  public void update() {
    List<DynamicBody> particles = mState.getDynamicBodies();
    List<Body> bodies = mState.getBodies();
    TreeSet<FutureCollisionData> q = new TreeSet<>();
    do {
      for (DynamicBody current : particles) {
        //TODO: tutaj powinno być zawężanie listy boidies i do closestCollision() powinny trafiać tylko te z którymi jest szansa na zderzenie
        Pair<Double, Body> closestToCurrent = mCollisionDetector.closestCollision(current, bodies);
        if (closestToCurrent != null) {
          q.add(new FutureCollisionData(current, closestToCurrent.first(), closestToCurrent.second())); //tree map automatycznie sortuje
        }
      }
      if(!q.isEmpty()) {mCollisionSolver.resolveCollision(q); }
    } while (!q.isEmpty()); //jak jest puste to znaczy że można przesunąć wszystkie w "normalny" sposób
    for (DynamicBody particle : particles) {
      particle.lastUpdate(mTimestep);
    }
  }

  private final SimulationState mState;
  private final double mTimestep;
  private final CollisionSolver mCollisionSolver;
  private final CollisionDetector mCollisionDetector;
}