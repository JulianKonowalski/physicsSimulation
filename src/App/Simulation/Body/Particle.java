package App.Simulation.Body;

import App.Simulation.Util.Vec2;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Particle extends Body {

  public Particle(boolean isStatic, Vec2 position, Vec2 velocity, Vec2 acceleration, double mass, int radius) {
    super(isStatic, position, velocity, acceleration, mass);
    if (radius < 0) { throw new IllegalArgumentException("Tried to set a negative particle radius"); }
    mVertices.add(new Vec2(0.0, 0.0));
    mRadius = radius;
  }

  @Override
  public void collide(Body other) { //placeholder
    mVelocity.invert();
  }

  @Override
  public void update(double timeStep) { //in seconds
    mVelocity.add(Vec2.scale(mAcceleration, timeStep));
    mPosition.add(Vec2.scale(mVelocity, timeStep));
  }

  @Override
  public Shape getShape() {
      return new Ellipse2D.Double(
        this.position().x() - this.radius(), 
        this.position().y() - this.radius(), 
        2.0 * this.radius(), 
        2.0 * this.radius()
        );
  }

  public int radius() { return mRadius; }

  private final int mRadius;
}