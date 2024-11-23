package App.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import App.FileHandlers.LoggingInterface;
import App.Simulation.Body.*;
import App.Simulation.Util.FutureCollisionData;
import App.Simulation.Util.Vec2;

public class Simulation {

  public Simulation(double timestep, LoggingInterface logger) {
    mTimestep = timestep;
    mCollisionSolver = new CollisionSolver();
    mCollisionDetector = new CollisionDetector(mTimestep);
    mLogger = logger;

    List<StaticBody> walls = new ArrayList<>();
    List<DynamicBody> particles = new ArrayList<>();

//    particles.add(new Particle(new Vec2(200, 100), new Vec2(-100, -300),40));
//    particles.add(new Particle(new Vec2(200, 200), new Vec2(-200, -400),40));

//    particles.add(new Particle(new Vec2(110, 100), new Vec2(-100, -100),10));
//    particles.add(new Particle(new Vec2(210, 200), new Vec2(-200, -200),32));

    particles.add(new Particle(new Vec2(100, 100), new Vec2(200, 400),12));
    particles.add(new Particle(new Vec2(200, 200), new Vec2(-200, 400),16));
    particles.add(new Particle(new Vec2(300, 300), new Vec2(400, 200),24));
    particles.add(new Particle(new Vec2(400, 400), new Vec2(-400, -200),32));
    particles.add(new Particle(new Vec2(1000, 100), new Vec2(200, 400),12));
    particles.add(new Particle(new Vec2(1000, 200), new Vec2(-200, 400),16));
    particles.add(new Particle(new Vec2(1000, 300), new Vec2(400, 200), 24));
    particles.add(new Particle(new Vec2(500, 400), new Vec2(-400, -200),32));
    particles.add(new Particle(new Vec2(500, 100), new Vec2(200, 400),12));
    particles.add(new Particle(new Vec2(500, 200), new Vec2(-200, 400),16));
    particles.add(new Particle(new Vec2(500, 300), new Vec2(400, 200), 24));
    particles.add(new Particle(new Vec2(500, 400), new Vec2(-400, -200),32));

    walls.add(new Line(new Vec2(0, 0), new Vec2(1280, 0), 2.0));
    walls.add(new Line(new Vec2(1280, 0), new Vec2(1280, 720), 2.0));
    walls.add(new Line(new Vec2(1280, 720), new Vec2(0, 720), 2.0));
    walls.add(new Line(new Vec2(0, 720), new Vec2(0, 0), 2.0));

    mState = new SimulationState(walls, particles);
  }

  public SimulationState getState() { return mState; }

  public void update() {
    List<DynamicBody> particles = mState.DynamicBodies();
    List<Body> bodies = mState.Bodies();
    TreeSet<FutureCollisionData> q = new TreeSet<>();
    for (DynamicBody current : particles) {
      List<Body> against = bodies.subList(bodies.indexOf(current) + 1, bodies.size());

      FutureCollisionData currentData = mCollisionDetector.closestCollision(current, against);
      if (currentData != null)
        q.add(currentData);
    }

    while (!q.isEmpty()){
      mLogger.log("Simulation", q.first().body()+ " collided with " + q.first().collider());

      mCollisionSolver.resolveCollision(q);
      FutureCollisionData resolvedData = q.pollFirst();
      if(resolvedData == null) {
        break;
      }

      List<Body> against = new ArrayList<>(bodies);
      against.remove(resolvedData.body());
      against.remove(resolvedData.collider());

      if(resolvedData.collider().type() == Body.Type.PARTICLE){
        FutureCollisionData againstData = mCollisionDetector.closestCollision((DynamicBody) resolvedData.collider(), against);
        if (againstData != null)
          q.add(againstData);
      }

      resolvedData = mCollisionDetector.closestCollision(resolvedData.body(), against);
      if (resolvedData != null)
        q.add(resolvedData);

    }
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