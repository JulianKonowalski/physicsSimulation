package App.Simulation.Util;

public class Circle {

  public Circle(Vec2 center, double radius) {
    mCenter = center;
    mRadius = radius;
  }

  public Vec2 getCenter() { return mCenter; }
  public double getRadius() { return mRadius; }

  public boolean intersects(Circle other) {
    return Vec2.distanceSquared(mCenter, other.mCenter) <  (mRadius + other.mRadius)*(mRadius + other.mRadius);
  }

  public boolean intersects(LineSegment line) {
    return  true;
  }

  private final Vec2 mCenter;
  private final double mRadius;
}
