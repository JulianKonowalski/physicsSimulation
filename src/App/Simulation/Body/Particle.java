package App.Simulation.Body;

import App.Simulation.CollisionSolver;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Objects;

public class Particle extends DynamicBody {

  public Particle(Vec2 position, Vec2 velocity, double mass, double radius) {
    super(position, velocity, mass);
    mRadius = radius;
  }

  public double radius() { return mRadius; }

  // DynamicBody interface
  @Override
  public Vec2 predictedPosition(double t) { return Vec2.add(mPosition, Vec2.scale(mVelocity, t)); }
  @Override
  public void updatePosition(double t) {
    mPosition.add(Vec2.scale(mVelocity, t));
    mInternalTime += t;
  }
  @Override
  public void lastUpdate(double timeStep) { //actual, global time step
    double timeRemaining = timeStep - mInternalTime;
    try {
      if(timeRemaining < 0) { throw new Exception("Negative remaining time - Skasuj windowsa"); }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }
    mPosition.add(Vec2.scale(mVelocity, timeRemaining));
    mInternalTime = 0;
  }
  @Override
  public void setVelocity(CollisionSolver.SolverKey solverKey, Vec2 velocity) {
    Objects.requireNonNull(solverKey);
    mVelocity = velocity;
    mDirection = Vec2.normalize(mVelocity);
  }

  // Body interface
  @Override
  public Type type() { return Type.PARTICLE; }
  @Override
  public Shape getShape() {
    return new Ellipse2D.Double(
            mPosition.x() - mRadius,
            mPosition.y() - mRadius,
            2.0 * mRadius,
            2.0 * mRadius);
  }
  @Override
  public LineSegment getLineSegment(double t) { return new LineSegment(mPosition, predictedPosition(t)); }

  double mRadius;
}
