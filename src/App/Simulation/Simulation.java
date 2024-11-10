package App.Simulation;

import App.Simulation.Body.NewLine;
import App.Simulation.Body.NewBody;
import App.Simulation.Body.NewParticle;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Pair;
import App.Simulation.Util.Vec2;

import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;

public class Simulation {

  public Simulation(double timeStep) {
    mTimeStep = timeStep;
    mCollisionSolver = new CollisionSolver();
    List<NewLine> walls = new ArrayList<>();
    List<NewParticle> particles = new ArrayList<>();
    particles.add(new NewParticle(new Vec2(100, 100), new Vec2(100, 200), 5.0, 10));
    particles.add(new NewParticle(new Vec2(200, 200), new Vec2(-100, 200), 5.0, 10));
    particles.add(new NewParticle(new Vec2(300, 300), new Vec2(200, 100), 5.0, 10));
    particles.add(new NewParticle(new Vec2(400, 400), new Vec2(-200, -100), 5.0, 10));

    walls.add(new NewLine(new Vec2(0, 0), new Vec2(1280, 0)));
    walls.add(new NewLine(new Vec2(1280, 0), new Vec2(1280, 720)));
    walls.add(new NewLine(new Vec2(1280, 720), new Vec2(0, 720)));
    walls.add(new NewLine(new Vec2(0, 720), new Vec2(0, 0)));

    mState = new SimulationState(walls, particles);
  }

  public SimulationState getState() {
    return mState;
  }

  public record ParticleData(NewParticle current, double timeToCollision,
                             NewBody other) implements Comparable<ParticleData> {
    @Override
    public int compareTo(ParticleData o) {
      int doubleCompare =  Double.compare(timeToCollision, o.timeToCollision);
      if (doubleCompare != 0) {
        return doubleCompare;
      }else{
        return Integer.compare(current.hashCode(), o.current.hashCode());
      }
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof ParticleData) {
        return current == ((ParticleData) obj).current;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return current.hashCode();
    }
  }

  public void update() {
    List<NewParticle> particles = mState.getParticles();
    List<NewBody> bodies = mState.getBodies();
    TreeSet<ParticleData> q = new TreeSet<>();
    do {
      for (NewParticle current : particles) {
        //TODO: tutaj powinno być zawężanie listy boidies i do closestCollision() powinny trafiać tylko te z którymi jest szansa na zderzenie
        Pair<Double, NewBody> closestToCurrent = closestCollision(current, bodies);
        if (closestToCurrent != null) // w sensie że zderza się z czymkolwiek
        {
          q.add(new ParticleData(current, closestToCurrent.first, closestToCurrent.second)); //tree map automatycznie sortuje
        }
      }
      resolveCollisions(q);
    } while (!q.isEmpty()); //jak jest puste to znaczy że można przesunąć wszystkie w "normalny" sposób
    for (NewParticle particle : particles) {
      particle.lastUpdate(mTimeStep);
    }
  }


  private Pair<Double, NewBody> closestCollision(NewParticle current, List<NewBody> bodies) {
    Pair<Double, NewBody> closest = null;
    for (NewBody other : bodies) {
      if (other == current) {
        continue;
      }
      Double timeToCollision = timeToCollision(current, other);
      if (timeToCollision != null && 0 < timeToCollision && timeToCollision < mTimeStep && (closest == null || timeToCollision < closest.first)) {
        closest = new Pair<>(timeToCollision, other);
      }
    }
    if (closest != null) {
      System.out.println("Collision with: " + closest.second);
    }
    return closest;
  }

  private Double timeToCollision(NewParticle current, NewBody other) {
    switch (other.type()) {
      case PARTICLE:
        //TODO
      case LINE:
        Double time = LineSegment.timeToIntersection(current.getLineSegment(mTimeStep), other.getLineSegment(mTimeStep));
        if (time != null) {
          return time * mTimeStep;
        }
    }
    return null;
  }

  private void resolveCollisions(TreeSet<ParticleData> q) {
    if (q.isEmpty()) {
      return;
    }
    ParticleData currentData = q.first();
    switch (currentData.other.type()) {
      case PARTICLE:
        //TODO
      case LINE:
        currentData.current.resolveLineCollision(currentData.timeToCollision, currentData.other.getLineSegment(mTimeStep));
    }
    q.remove(currentData);
  }

  private final SimulationState mState;
  private final CollisionSolver mCollisionSolver;
  private final double mTimeStep;
}