package App.Simulation.Body;

import App.Simulation.Util.Vec2;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class NewParticle implements NewBody {

  public NewParticle(Vec2 position, Vec2 velocity, double mass, double radius) {
    mPosition = position;
    mVelocity = velocity;
    mMass = mass;
    mRadius = radius;
  }

  @Override
  public boolean isDynamic() {
    return true;
  }
  @Override
  public BodyType type() {
    return BodyType.PARTICLE;
  }
  @Override
  public Vec2 position() {
    return mPosition;
  }
  @Override
  public Vec2 velocity() {
    return mVelocity;
  }
  @Override
  public double mass() {
    return mMass;
  }
  @Override
  public Vec2 predictedPosition(double t) {
    return Vec2.add(mPosition, Vec2.scale(mVelocity, t));
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
  double mMass;
  double mRadius;
}
