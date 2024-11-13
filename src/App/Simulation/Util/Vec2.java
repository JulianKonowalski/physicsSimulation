package App.Simulation.Util;

public class Vec2 {

  public Vec2(double x, double y) { mX = x; mY = y; }
  public double x() { return mX; }
  public double y() { return mY; }

  //operations on this instance
  public void set(double x, double y) { mX = x; mY = y; }
  public void add(Vec2 other) { mX += other.x(); mY += other.y(); }
  public void subtract(Vec2 other) {  mX -= other.x(); mY -= other.y(); }
  public void scale(double scalar) { mX *= scalar; mY *= scalar; }
  public void negate() { mX = -mX; mY = -mY; }
  public void perpendicularClockwise() {
    double tmp = mY;
    mY = -mX;
    mX = tmp;
  }
  public void perpendicularCounterClockwise() {
    double tmp = mY;
    mY = mX;
    mX = -tmp;
  }
  public void rotate(double angle) {
    double x = mX * Math.cos(angle) - mY * Math.sin(angle);
    double y = mX * Math.sin(angle) + mY * Math.cos(angle);
    mX = x;
    mY = y;
  }
  public void normalize(){
    double len = Vec2.length(this);
    if (len != 0) {
      mX /= len;
      mY /= len;
    }
  }

  //operations without modifying this instance
  public static Vec2 zero() { return new Vec2(0, 0); }
  public static Vec2 add(Vec2 v1, Vec2 v2) { return new Vec2(v1.x() + v2.x(), v1.y() + v2.y()); }
  public static Vec2 subtract(Vec2 v1, Vec2 v2) { return new Vec2(v1.x() - v2.x(), v1.y() - v2.y()); }
  public static Vec2 scale(Vec2 v1, double scalar) { return new Vec2(v1.x() * scalar, v1.y() * scalar); }
  public static boolean equals(Vec2 v1, Vec2 v2) { return v1.x() == v2.x() && v1.y() == v2.y(); }
  public static double dotProduct(Vec2 v1, Vec2 v2) { return v1.x() * v2.x() + v1.y() * v2.y(); }
  public static double crossProduct(Vec2 v1, Vec2 v2) { return v1.x() * v2.y() - v1.y() * v2.x(); }
  public static double lengthSquared(Vec2 v1) { return v1.x() * v1.x() + v1.y() * v1.y(); }
  public static double length(Vec2 v1) { return Math.sqrt(v1.x() * v1.x() + v1.y() * v1.y()); }
  public static double distanceSquared(Vec2 v1, Vec2 v2) { return (v1.x() - v2.x()) * (v1.x() - v2.x()) + (v1.y() - v2.y()) * (v1.y() - v2.y()); }
  public static double distance(Vec2 v1, Vec2 v2) { return (double)Math.sqrt((v1.x() - v2.x()) * (v1.x() - v2.x()) + (v1.y() - v2.y()) * (v1.y() - v2.y())); }
  public static double angle(Vec2 v1, Vec2 v2) { return (double)Math.acos(dotProduct(v1, v2) / (Vec2.length(v1) * Vec2.length(v2))); }
  public static Vec2 negate(Vec2 v) { return new Vec2(-v.x(), -v.y()); }
  public static Vec2 perpendicularClockwise(Vec2 v) { return new Vec2(v.y(), -v.x()); }
  public static Vec2 perpendicularCounterClockwise(Vec2 v) { return new Vec2(-v.y(), v.x()); }
  public static Vec2 rotate(Vec2 v, double angle) {
    double x = v.x() * Math.cos(angle) - v.y() * Math.sin(angle);
    double y = v.x() * Math.sin(angle) + v.y() * Math.cos(angle);return new Vec2(x, y);
  }
  public static Vec2 normalize(Vec2 v){
    double len = Vec2.length(v);
    if (len != 0) {
      return new Vec2(v.x() / len, v.y() / len);
    }
    return new Vec2(0, 0);
  }
  public static Vec2 project(Vec2 vec1, Vec2 vec2) {
    Vec2 vec2Normalized = Vec2.normalize(vec2);
    return Vec2.scale(vec2Normalized, Vec2.dotProduct(vec1, vec2Normalized));
  }

  private double mX;
  private double mY;
}
