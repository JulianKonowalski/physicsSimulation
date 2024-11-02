package App.Simulation.Body;

import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;

import java.util.Objects;

public class Particle extends DynamicBody {

  public Particle(Vec2 position, Vec2 velocity, Vec2 acceleration, float mass, int radius, float timeStep) {
    super(position, velocity, acceleration, mass, timeStep);
    if (radius < 0) {
      throw new IllegalArgumentException("Tried to set a negative particle radius");
    }
    mRadius = radius;
  }

  @Override
  public void update(float timeStep) {
    if(Vec2.equals(mVelocity, Vec2.zero())) {
      return;
    }
    if(Math.abs(mVelocity.x()) < 0.01) {
      mVelocity = Vec2.zero();
      return;
    }
    mVelocity = Vec2.add(mVelocity, Vec2.scale(mAcceleration, timeStep));
    mPosition = Vec2.add(mPosition, Vec2.scale(mVelocity, timeStep));

  }

  public int radius() {
    return mRadius;
  }

  private final int mRadius;
}