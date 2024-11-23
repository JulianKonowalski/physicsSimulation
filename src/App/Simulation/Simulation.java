package App.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import App.FileHandlers.LoggingInterface;
import App.Simulation.Body.*;
import App.Simulation.Util.FutureCollisionData;
import App.Simulation.Util.Vec2;

public class Simulation {

  public Simulation(SimulationState initialState, double timestep, LoggingInterface logger) {
    mState = initialState;
    mTimestep = timestep;
    mCollisionDetector = new CollisionDetector(mTimestep);
    mCollisionSolver = new CollisionSolver();
    mLogger = logger;
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