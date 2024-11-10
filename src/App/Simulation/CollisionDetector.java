package App.Simulation;

import App.Simulation.Body.Body;
import App.Simulation.Body.Particle;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Pair;
import App.Simulation.Util.Vec2;
import java.util.List;

public class CollisionDetector {

  public CollisionDetector(double timestep) { mTimestep = timestep; }

  public Pair<Double, Body> closestCollision(Particle current, List<Body> bodies) {
    Pair<Double, Body> closest = null;
    for (Body other : bodies) {
      if (other == current) { continue; }
      Double timeToCollision = timeToCollision(current, other);
      if (timeToCollision != null && timeToCollision > 0 && timeToCollision < mTimestep && (closest == null || timeToCollision < closest.first)) {
        closest = new Pair<>(timeToCollision, other);
      }
    }
    if (closest != null) { System.out.println(current  + " collided with " + closest.second); } //TODO: delete this, it's slow af
    return closest;
  }

  private Double timeToCollision(Particle current, Body other) {
    Double time = LineSegment.timeToIntersection(current.getLineSegment(mTimestep), other.getLineSegment(mTimestep));
    if(time == null) { return null; }
    switch (other.type()) {
      case PARTICLE -> { return particleParticleCollision(current, (Particle)other, time); }
      case LINE -> { return time * mTimestep; }
      default -> { return null; }
    }
  }

  private Double particleParticleCollision(Particle particle1, Particle particle2, Double time) {
    Vec2 predictedPosition1 = particle1.predictedPosition(time);
    Vec2 predictedPosition2 = particle2.predictedPosition(time);
    if(Vec2.distance(predictedPosition1, predictedPosition2) <= particle1.radius() + particle2.radius()) {
      System.out.println(time*mTimestep);
      return time * mTimestep;
    }
    return null;
  }

  private final double mTimestep;
}
