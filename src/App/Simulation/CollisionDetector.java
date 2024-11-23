package App.Simulation;

import App.Simulation.Body.Body;
import App.Simulation.Body.DynamicBody;
import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;
import java.util.List;

import App.Simulation.Util.FutureCollisionData;
import App.Simulation.Util.LineSegment;

public class CollisionDetector {

  public CollisionDetector(double timestep) {
    mTimestep = timestep;
  }

  public FutureCollisionData closestCollision(DynamicBody current, List<Body> against) {
    FutureCollisionData closest = null;
    for (Body other : against) {
      if (other == current) { continue; }
      Double timeOfCollision = timeOfCollision((Particle) current, other);
      if (timeOfCollision != null && current.timeInternal() <= timeOfCollision && timeOfCollision < mTimestep && (closest == null || timeOfCollision < closest.timeOfCollision())) {
//        if(closest != null && closest.collider().type() == Body.Type.LINE && timeOfCollision == closest.timeOfCollision()) {
//          Line closestLine = (Line) closest.collider();
//          Line otherLine = (Line) other;
//          Line cornerLine = constructCornerLine(closestLine, otherLine, current.position());
//          closest = new FutureCollisionData(current, timeOfCollision, cornerLine);
//        }
//        else {
          closest = new FutureCollisionData(current, timeOfCollision, other);
//        }
      }
    }
    return closest;
  }

  private Double timeOfCollision(Particle particle, Body collider) {
    switch (collider.type()) {
      case PARTICLE -> { return timeOfParticleParticleCollision(particle, (Particle) collider); }
      case LINE -> { return timeOfLineParticleCollision(particle, (Line) collider); }
      default -> { return null; }
    }
  }

  public Double timeOfParticleParticleCollision(Particle particle, Particle against) {
    Particle particle1 = particle;
    Particle particle2 = against;
    double timeInitial = particle.timeInternal();
    Vec2 V = Vec2.subtract(particle1.velocity(), particle2.velocity()); //delta V
    Vec2 S; //delta S

    if (particle.timeInternal() != against.timeInternal()) { //setting S to be difference between positions at the same initial time
      particle1 = against.timeInternal() < particle.timeInternal() ? particle : against;
      particle2 = particle1 == particle ? against : particle;
      timeInitial = particle1.timeInternal();
      Vec2 alignedPosition = particle2.predictedPosition(particle1.timeInternal() - particle2.timeInternal());
      S = Vec2.subtract(particle1.position(), alignedPosition);
    } else {
      S = Vec2.subtract(particle1.position(), particle2.position());
    }

    double R = particle1.radius() + particle2.radius();
    double RSquared = R * R;
    double crossProduct = Vec2.crossProduct(S, V);
    double crossProductSquared = crossProduct * crossProduct;
    double VNormSquared = Vec2.lengthSquared(V);

    double discriminant = RSquared * VNormSquared - crossProductSquared;
    if(discriminant <= 0) { return null; }

    double dotProduct = Vec2.dotProduct(S, V);
    double inverseVNormSquared = 1.0 / VNormSquared;
    double discriminantSqrt = Math.sqrt(discriminant);

    double t1 = (-dotProduct - discriminantSqrt) * inverseVNormSquared;
//    double t2 = (-dotProduct + discriminantSqrt) * inverseVNormSquared;

    return timeInitial + t1;
  }

  private Double timeOfLineParticleCollision(Particle particle, Line against) {

    Vec2 closestPointOffset = Vec2.scale(Vec2.negate(Vec2.normalize(against.NormalToPoint(particle.position()))), particle.radius());
    double timeInternal = particle.timeInternal();
    double timeRemaining = mTimestep - particle.timeInternal();

    LineSegment particleDisplacement = particle.getLineSegment(timeRemaining);
    LineSegment closestPointDisplacement = LineSegment.move(particleDisplacement, closestPointOffset);

    Double timeToIntersection = LineSegment.timeToIntersection(closestPointDisplacement, against);
    if (timeToIntersection == null) { return null; }
    return timeInternal + timeToIntersection * timeRemaining;
  }

  private Line constructCornerLine(Line line1, Line line2, Vec2 position) {
    Vec2 intersection = Line.intersection(line1, line2);
    Vec2 relativePosition = Vec2.subtract(position, intersection);
    Vec2 normalizedLine1Vector = Vec2.normalize(Vec2.subtract(line1.p2(), line1.p1()));
    Vec2 normalizedLine2Vector = Vec2.normalize(Vec2.subtract(line2.p2(), line2.p1()));
    normalizedLine1Vector = Vec2.dotProduct(relativePosition, normalizedLine1Vector) > 0 ? normalizedLine1Vector : Vec2.negate(normalizedLine1Vector);
    normalizedLine2Vector = Vec2.dotProduct(relativePosition, normalizedLine2Vector) > 0 ? normalizedLine2Vector : Vec2.negate(normalizedLine2Vector);
    Vec2 composition = Vec2.subtract(normalizedLine1Vector, normalizedLine2Vector);
    return new Line(Vec2.subtract(intersection, composition), Vec2.add(intersection, composition), line1.thickness()); //trzeba to thickness wywalić
  }

  private final double mTimestep;
}
