package App.Simulation;

import App.FileHandlers.LoggingInterface;
import App.Simulation.Body.*;
import App.Simulation.Util.FutureCollisionData;
import App.Simulation.Util.Vec2;
import App.Util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Simulation {

  public Simulation(double timestep, LoggingInterface logger) {
    mTimestep = timestep;
    mCollisionSolver = new CollisionSolver();
    mCollisionDetector = new CollisionDetector(mTimestep);
    mLogger = logger;

    List<StaticBody> walls = new ArrayList<>();
    List<DynamicBody> particles = new ArrayList<>();

    particles.add(new Particle(new Vec2(100, 100), new Vec2(200, 400), 10, 10));
    particles.add(new Particle(new Vec2(200, 200), new Vec2(-200, 400),  16, 16));
    particles.add(new Particle(new Vec2(300, 300), new Vec2(400, 200),  24, 24));
    particles.add(new Particle(new Vec2(400, 400), new Vec2(-400, -200), 32, 32));

    particles.add(new Particle(new Vec2(1000, 100), new Vec2(200, 400), 10, 10));
    particles.add(new Particle(new Vec2(1000, 200), new Vec2(-200, 400),  16, 16));
    particles.add(new Particle(new Vec2(1000, 300), new Vec2(400, 200),  24, 24));
    particles.add(new Particle(new Vec2(1000, 400), new Vec2(-400, -200), 32, 32));


    walls.add(new Line(new Vec2(0, 0), new Vec2(1280, 0), 2.0));
    walls.add(new Line(new Vec2(1280, 0), new Vec2(1280, 720), 2.0));
    walls.add(new Line(new Vec2(1280, 720), new Vec2(0, 720), 2.0));
    walls.add(new Line(new Vec2(0, 720), new Vec2(0, 0), 2.0));

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
      if(!q.isEmpty()) { 
        mLogger.log("Simulation", q.first().body()+ " collided with " + q.first().collider());
        mCollisionSolver.resolveCollision(q); 
      }
    } while (!q.isEmpty()); //jak jest puste to znaczy że można przesunąć wszystkie w "normalny" sposób
    for (DynamicBody particle : particles) {
      particle.lastUpdate(mTimestep);
    }
  }

  private final SimulationState mState;
  private final double mTimestep;
  private final LoggingInterface mLogger;
  private final CollisionSolver mCollisionSolver;
  private final CollisionDetector mCollisionDetector;
}