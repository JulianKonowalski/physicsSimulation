package App.Simulation.Body;

import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;

public class Line extends LineSegment implements StaticBody {

  public Line(Vec2 P1, Vec2 P2, double thickness) {
    super(P1, P2);
    mThickness = thickness;
//    mShape = constructShape(mThickness);
    A = p1().y() - p2().y();
    B = p2().x() - p1().x();
    C = - A * p1().x() - B * p1().y();
  }

  @Override
  public Type type() { return Type.LINE; }
  @Override
  public LineSegment getLineSegment(double t) { return this; }
  public double A() { return A; }
  public double B() { return B; }
  public double C() { return C; }
  public double thickness() { return mThickness; } //TODO: delete if possible

  public static Vec2 intersection(Line line1, Line line2) {
    double a1 = line1.A(), b1 = line1.B(), c1 = line1.C();
    double a2 = line2.A(), b2 = line2.B(), c2 = line2.C();

    double denominator = a1 * b2 - a2 * b1;

    if (denominator == 0) {
      throw new IllegalArgumentException("Lines are parallel or coincident, no unique intersection point.");
    }

    double x = (b1 * c2 - b2 * c1) / denominator;
    double y = (c1 * a2 - c2 * a1) / denominator;

    return new Vec2(x, y);
  }

  public Vec2 NormalToPoint(Vec2 point) {
    double V = A * point.x() + B * point.y() + C;
    if (V == 0) {
      return null;
    }
    return V > 0 ? new Vec2(A, B) : new Vec2(-A, -B);
  }

  private final double A, B, C;
  private final double mThickness;
//  private final Path2D mShape;
}