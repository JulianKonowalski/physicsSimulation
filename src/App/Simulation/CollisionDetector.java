package App.Simulation;

import App.Simulation.Body.Body;
import App.Simulation.Body.DynamicBody;
import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Pair;
import App.Simulation.Util.Vec2;

import java.util.List;

public class CollisionDetector {

  public CollisionDetector(double timestep) {
    mTimestep = timestep;
  }

  public Pair<Double, Body> closestCollision(DynamicBody current, List<Body> bodies) {
    Pair<Double, Body> closest = null;
    for (Body other : bodies) {
      if (other == current) { continue; }
      //assumes current is a particle
      Double timeToCollision = timeToCollision((Particle) current, other);
      if (timeToCollision != null && 0 < timeToCollision && timeToCollision <= mTimestep && (closest == null || timeToCollision < closest.first())) {
        closest = new Pair<>(timeToCollision, other);
      }
    }
    if (closest != null) {
      System.out.println(current + " collided with " + closest.second());
    } //TODO: delete this, it's slow af
    return closest;
  }

  private Double timeToCollision(Particle particle, Body collider) {
    switch (collider.type()) {
      case PARTICLE -> { return timeToParticleParticleCollision(particle, (Particle) collider); }
      case LINE -> { return timeToLineParticleCollision(particle, (Line) collider); }
      default -> { return null; }
    }
  }

  public Double timeToParticleParticleCollision(Particle particle1, Particle particle2) {

    Vec2 V = Vec2.subtract(particle1.velocity(), particle2.velocity()); //delta V
    Vec2 S = Vec2.subtract(particle1.position(), particle2.position()); //delta S
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
    double t2 = (-dotProduct + discriminantSqrt) * inverseVNormSquared;

    return Math.min(t1, t2);
  }

  private Double timeToLineParticleCollision(Particle particle, Line line) {

    Vec2 closestPointOffset = Vec2.scale(Vec2.negate(Vec2.normalize(line.NormalToPoint(particle.position()))), particle.radius());
    LineSegment particleDisplacement = particle.getLineSegment(mTimestep);
    LineSegment closestPointDisplacement = LineSegment.move(particleDisplacement, closestPointOffset);
    Double timeToIntersection = timeToIntersection(closestPointDisplacement, line);
    if (timeToIntersection == null) { return null; }
    return timeToIntersection * mTimestep;
  }

  // returns t in [0, 1] if there is an intersection
  private static Double timeToIntersection(LineSegment line, LineSegment against) {
    Vec2 r = Vec2.subtract(line.p2(), line.p1());
    Vec2 s = Vec2.subtract(against.p2(), against.p1());

    Vec2 p1 = line.p1();
    Vec2 p2 = line.p2();
    Vec2 q1 = against.p1();
    Vec2 q2 = against.p2();

    double tNumerator =Vec2.crossProduct(Vec2.subtract(q1, p1), s);
    double denominator = Vec2.crossProduct(r, s);

    if (tNumerator == 0 && denominator == 0) {
      // They are collinear

      // Do they touch? (Are any of the points equal?)
      if (Vec2.equals(p1, q1) || Vec2.equals(p1, q2)) {
        return 0.0;
      }
      if (Vec2.equals(p1, q1) || Vec2.equals(p2, q2)) {
        return 1.0;
      }
      // Do they overlap? (Are all the point differences in either direction the same sign)
      Double time = timeForCollinear(p1.x(), p2.x(), q1.x(), q2.x());
      if (time != null) {
        return time;
      }
      time = timeForCollinear(p1.y(), p2.y(), q1.y(), q2.y());
      return time;
    }

    if (denominator == 0) {
      // lines are parallel
      return null;
    }

    double t = tNumerator / denominator;
    double u = Vec2.crossProduct(Vec2.subtract(q1, p1), r) / denominator;

    return  (0 <= u) && (u <= 1) ? t : null; //wydaje się że ten warunek starcza, ale trzeba przetestować na liniach które są w pudełku
//    return (t >= 0) && (t <= 1) && (u >= 0) && (u <= 1) ? t : null;
  }

  private static boolean allEqual(boolean... values) {
    for (int i = 1; i < values.length; i++) {
      if (values[i] != values[0]) {
        return false;
      }
    }
    return true;
  }

  private static Double timeForCollinear(double A, double B, double C, double D) { //A is starting point of move
    if (allEqual(C - A < 0, C - B < 0, D - A < 0, D - B < 0)) {
      return null;
    }
    return Math.min(Math.abs(A - C), Math.abs(A - D)) / Math.abs(B - A);
  }

  private final double mTimestep;
}
