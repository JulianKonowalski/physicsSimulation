package App.Simulation.Body;

import App.Simulation.Util.Vec2;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

public abstract class Body {

  public Body(boolean isStatic, Vec2 position, Vec2 velocity, Vec2 acceleration, double mass) {
    if (mass < 0) { throw new IllegalArgumentException("Tried to set a negative body mass"); }
    mIsStatic = isStatic;
    mPosition = position;
    mMass = mass;
    mVertices = new ArrayList<>();
    if (mIsStatic) {
      mVelocity = Vec2.zero();
      mAcceleration = Vec2.zero();
    } else {
      mVelocity = velocity;
      mAcceleration = acceleration;
    }
  }

  public Vec2 getFuturePosition(double timeStep) {
    return Vec2.add(mPosition, Vec2.scale(mVelocity, timeStep));
  }

  public Vec2 position() { return mPosition; }
  public Vec2 velocity() { return mVelocity; }
  public Vec2 acceleration() { return mAcceleration; }
  public double mass() { return mMass; }
  public List<Vec2> vertices() { return mVertices; }
  
  public void setPosition(Vec2 position) { mPosition = position; }
  public void setVelocity(Vec2 velocity) {  mVelocity = mIsStatic != true ? velocity : mVelocity; }
  public void setAcceleration(Vec2 acceleration) { mAcceleration = mIsStatic != true ? acceleration : mAcceleration; }

  public static double distance(Body body1, Body body2) {
    return new Vec2(body1.position().x() - body2.position().x(), body1.position().y() - body2.position().y()).length();
  }
  
  public abstract void collide(Body other); /* toOverride */
  public abstract void update(double timeStep); /* toOverride */
  public abstract Shape getShape(); /* toOverride */

  protected Vec2 mPosition;
  protected Vec2 mVelocity;
  protected Vec2 mAcceleration;
  protected final double mMass;
  protected final List<Vec2> mVertices;
  protected final boolean mIsStatic;
}
