package App.Simulation.Body;

import App.Simulation.CollisionSolver;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Objects;

public class Particle implements Body {

  public Particle(Vec2 position, Vec2 velocity, double mass, double radius) {
    mPosition = position;
    mVelocity = velocity;
    mDirection = Vec2.normalize(mVelocity);
    mMass = mass;
    mRadius = radius;
  }

  @Override
  public boolean isDynamic() { return true; }
  @Override
  public BodyType type() { return BodyType.PARTICLE; }
  @Override
  public Vec2 position() { return mPosition; }
  @Override
  public Vec2 velocity() { return mVelocity; }
  @Override
  public double mass() { return mMass; }
  @Override
  public Vec2 predictedPosition(double t) { return Vec2.add(mPosition, Vec2.scale(mVelocity, t)); }
  @Override
  public LineSegment getLineSegment(double t) { return new LineSegment(Vec2.add(mPosition, Vec2.scale(mDirection, mRadius)), Vec2.add(predictedPosition(t), Vec2.scale(mDirection, mRadius))); }

  public double radius() { return mRadius; }
  
  public void setVelocity(CollisionSolver.SolverKey solverKey, Vec2 velocity) {
    Objects.requireNonNull(solverKey);
    mVelocity = velocity;
    mDirection = Vec2.normalize(mVelocity);
  }

  public void addToInternalTime(CollisionSolver.SolverKey solverKey, double timeDelta) {
    Objects.requireNonNull(solverKey);
    mInternalTime += timeDelta;
  }

  public void updatePosition(double t) { mPosition.add(Vec2.scale(mVelocity, t)); }

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
  public Shape getShape() {
    return new Ellipse2D.Double(
            mPosition.x() - mRadius,
            mPosition.y() - mRadius,
            2.0 * mRadius,
            2.0 * mRadius
    );
  }

  Vec2 mPosition;
  Vec2 mVelocity;
  Vec2 mDirection;
  double mMass;
  double mRadius;
  double mInternalTime;
}