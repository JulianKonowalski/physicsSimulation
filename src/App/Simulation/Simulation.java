package App.Simulation;

import App.Simulation.Body.NewLine;
import App.Simulation.Body.NewBody;
import App.Simulation.Body.NewParticle;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;

public class Simulation {

  public Simulation(double timeStep) {
    mTimeStep = timeStep;
    mCollisionSolver = new CollisionSolver();
    List<NewBody> inititalState = new ArrayList<>();
    inititalState.add(new NewParticle(new Vec2(100, 100), new Vec2(100, 200), 5.0, 10));
    //inititalState.add(new Particle(false, new Vec2(100, 250), Vec2.zero(), Vec2.zero(), 5.0, 100));

    inititalState.add(new NewLine(new Vec2(0, 0), new Vec2(1280, 0)));
    inititalState.add(new NewLine(new Vec2(1280, 0), new Vec2(1280, 720)));
    inititalState.add(new NewLine(new Vec2(1280, 720), new Vec2(0, 720)));
    inititalState.add(new NewLine(new Vec2(0, 720), new Vec2(0, 0)));

    mState = new SimulationState(inititalState);
  }

  public SimulationState getState() {
    return mState;
  }


  static private class Pair<T, U> {
    public T first;
    public U second;

    public Pair(T first, U second) {
      this.first = first;
      this.second = second;
    }
  }

  public void update() {
    List<NewBody> bodies = mState.getBodies();
    TreeMap<Double, Pair<NewBody, NewBody>> q = new TreeMap<>();
    do {
      for (NewBody current : bodies) {
        Pair<Double, NewBody> closestToCurrent = closestCollision(current, bodies);
        if (closestToCurrent != null) // w sensie że zderza się z czymkolwiek
        {
          q.put(closestToCurrent.first, new Pair<>(current, closestToCurrent.second)); //tree map automatycznie sortuje
        }
      }
      resolveCollisions(q);
    } while (!q.isEmpty()); //jak jest puste to znaczy że można przesunąć wszystkie w "normalny" sposób
    for (NewBody b : bodies) {
      //update remaining time
    }
  }


  private Pair<Double, NewBody> closestCollision(NewBody current, List<NewBody> bodies) {
    if (!current.isDynamic()) {
      return null;
    }
    Pair<Double, NewBody> closest = null;
    for (NewBody other : bodies) {
      if (other == current) {
        continue;
      }
      Double timeToCollision = timeToCollision(current, other);
      if (0 < timeToCollision && timeToCollision < mTimeStep && (closest == null || timeToCollision < closest.first)){
        closest = new Pair<>(timeToCollision, other);
      }
    }
    return closest;
  }

  private Double timeToCollision(NewBody current, NewBody other) {
    LineSegment line = new LineSegment(current.position(), current.predictedPosition(mTimeStep));
    switch (other.type()){
      case PARTICLE:
        //TODO
      case LINE:
        //TODO
    }
    return 0.0;
  }

  private void resolveCollisions(TreeMap<Double, Pair<NewBody, NewBody>> q) {
    if (q.isEmpty()) {
      return;
    }
     Map.Entry<Double, Pair<NewBody, NewBody>> TimeCurrentOther = q.firstEntry();

  }

//  private void checkForCollisions(Body body, List<Body> bodies) {
//    for (Body other : bodies) {
//      if (other == body) {
//        continue;
//      }
//      if (body.intersects(other)) {
//        body.accept(mCollisionSolver, other);
//      }
//    }
//  }

  private final SimulationState mState;
  private final CollisionSolver mCollisionSolver;
  private final double mTimeStep;
}