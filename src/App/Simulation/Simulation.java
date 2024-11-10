package App.Simulation;

import App.Simulation.Body.Body;
import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.Util.FutureCollisionData;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Pair;
import App.Simulation.Util.Vec2;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Simulation {

  public Simulation(double timeStep) {
    mTimeStep = timeStep;
    mCollisionSolver = new CollisionSolver();
    List<Line> walls = new ArrayList<>();
    List<Particle> particles = new ArrayList<>();

    particles.add(new Particle(new Vec2(100, 100), new Vec2(100, 200), 5.0, 10));
    particles.add(new Particle(new Vec2(200, 200), new Vec2(-100, 200), 5.0, 10));
    particles.add(new Particle(new Vec2(300, 300), new Vec2(200, 100), 5.0, 10));
    particles.add(new Particle(new Vec2(400, 400), new Vec2(-200, -100), 5.0, 10));

    walls.add(new Line(new Vec2(0, 0), new Vec2(1280, 0), 2.0));
    walls.add(new Line(new Vec2(1280, 0), new Vec2(1280, 720), 2.0));
    walls.add(new Line(new Vec2(1280, 720), new Vec2(0, 720), 2.0));
    walls.add(new Line(new Vec2(0, 720), new Vec2(0, 0), 2.0));

    mState = new SimulationState(walls, particles);
  }

  public SimulationState getState() { return mState; }

  public void update() {
    List<Particle> particles = mState.getParticles();
    List<Body> bodies = mState.getBodies();
    TreeSet<FutureCollisionData> q = new TreeSet<>();
    do {
      for (Particle current : particles) {
        //TODO: tutaj powinno być zawężanie listy boidies i do closestCollision() powinny trafiać tylko te z którymi jest szansa na zderzenie
        Pair<Double, Body> closestToCurrent = closestCollision(current, bodies);
        if (closestToCurrent != null) {
          q.add(new FutureCollisionData(current, closestToCurrent.first, closestToCurrent.second)); //tree map automatycznie sortuje
        }
      }
      if(!q.isEmpty()) { mCollisionSolver.resolveCollision(q); }
    } while (!q.isEmpty()); //jak jest puste to znaczy że można przesunąć wszystkie w "normalny" sposób
    for (Particle particle : particles) {
      particle.lastUpdate(mTimeStep);
    }
  }

  private Pair<Double, Body> closestCollision(Particle current, List<Body> bodies) {
    Pair<Double, Body> closest = null;
    for (Body other : bodies) {
      if (other == current) { continue; }
      Double timeToCollision = timeToCollision(current, other);
      if (timeToCollision != null && timeToCollision > 0 && timeToCollision < mTimeStep && (closest == null || timeToCollision < closest.first)) {
        closest = new Pair<>(timeToCollision, other);
      }
    }
    if (closest != null) { System.out.println(current  + " collided with " + closest.second); } //TODO: delete this, it's slow af
    return closest;
  }

  private Double timeToCollision(Particle current, Body other) {
    switch (other.type()) {
      case PARTICLE -> { /* TODO: implemet time to collision with a particle */ }
      case LINE -> {
        Double time = LineSegment.timeToIntersection(current.getLineSegment(mTimeStep), other.getLineSegment(mTimeStep));
        if (time != null) { return time * mTimeStep; }
      }
    }
    return null;
  }

  private final SimulationState mState;
  private final double mTimeStep;
  private final CollisionSolver mCollisionSolver;
}