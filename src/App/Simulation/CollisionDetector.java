package App.Simulation;

import App.Simulation.Body.Body;
import App.Simulation.Body.Line;
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

  private Double timeToCollision(Particle particle, Body collider) {
    switch (collider.type()) {
      case PARTICLE -> { return timeToParticleParticleCollision(particle, (Particle)collider); }
      case LINE -> { return timeToLineParticleCollision(particle, (Line)collider); }
      default -> { return null; }
    }
  }

  private Double timeToParticleParticleCollision(Particle particle1, Particle particle2) {
    Double timeToIntersection = LineSegment.timeToIntersection(particle1.getLineSegment(mTimestep), particle2.getLineSegment(mTimestep));
    if(timeToIntersection == null) { return null; }
    Vec2 predictedPosition1 = particle1.predictedPosition(timeToIntersection);
    Vec2 predictedPosition2 = particle2.predictedPosition(timeToIntersection);
    if(Vec2.distance(predictedPosition1, predictedPosition2) <= particle1.radius() + particle2.radius()) {
      return timeToIntersection * mTimestep;
    }
    return null;
  }

  private Double timeToLineParticleCollision(Particle particle, Line line) {
    Double timeToIntersection = LineSegment.timeToIntersection(particle.getLineSegment(mTimestep), line.getLineSegment(mTimestep));
    if(timeToIntersection == null) { return null; }

    /* TODO:something is wrong here - the timeToRewind sometimes is greater than timeToIntersection */
    Vec2 normalCollisionVector = new Vec2(line.p1().y() - line.p2().y(), line.p2().x() - line.p1().x());
    double dotProduct = Vec2.dotProduct(particle.velocity(), normalCollisionVector);
    if(dotProduct > 0) {
      dotProduct = -1 * dotProduct;
      normalCollisionVector.negate();
    }
    Double timeToRewind = particle.radius() / Vec2.length(Vec2.scale(normalCollisionVector, dotProduct / Vec2.lengthSquared(normalCollisionVector)));

    return (timeToIntersection - timeToRewind) * mTimestep;
  }

  private final double mTimestep;
}
