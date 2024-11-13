package App.Simulation.Body;

import App.Simulation.CollisionSolver;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;

import java.awt.*;

abstract public class DynamicBody implements Body {

  public DynamicBody(Vec2 position, Vec2 velocity, double mass) {
    mMass = mass;
    mVelocity = velocity;
    mDirection = Vec2.normalize(mVelocity);
    mPosition = position;
    mInternalTime = 0;
  }

  public Vec2 position() { return mPosition; }
  public Vec2 velocity() { return mVelocity; }
  public double mass() { return mMass; }

  // DynamicBody interface
  abstract public Vec2 predictedPosition(double t);
  abstract public void updatePosition(double t);
  abstract public void lastUpdate(double timeStep);
  abstract public void setVelocity(CollisionSolver.SolverKey solverKey, Vec2 velocity);

  // Body interface
  @Override
  abstract public Type type();
  @Override
  abstract public LineSegment getLineSegment(double t);
  @Override
  abstract public Shape getShape();

  double mMass;
  Vec2 mVelocity;
  Vec2 mDirection;
  Vec2 mPosition;
  double mInternalTime;
}
