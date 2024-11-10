package App.Simulation.Util;

public class LineSegment {
  public LineSegment(Vec2 P1, Vec2 P2) {
    mP1 = P1;
    mP2 = P2;
  }

  public Vec2 p1() {
    return mP1;
  }

  public Vec2 p2() {
    return mP2;
  }

  // returns t in [0, 1] if there is an intersection
  public static Double timeToIntersection(LineSegment line, LineSegment against) {
    Vec2 r = Vec2.subtract(line.p2(), line.p1());
    Vec2 s = Vec2.subtract(against.p2(), against.p1());

    Vec2 p1 = line.p1();
    Vec2 p2 = line.p2();
    Vec2 q1 = against.p1();
    Vec2 q2 = against.p2();

    double uNumerator = Vec2.crossProduct(Vec2.subtract(q1, p1), r);
    double denominator = Vec2.crossProduct(r, s);

    if (uNumerator == 0 && denominator == 0) {
      // They are collinear

      // Do they touch? (Are any of the points equal?)
      if (Vec2.equals(p1, q1) || Vec2.equals(p1, q2)) {
        return 0.0;
      }
      if (Vec2.equals(p1, q1) || Vec2.equals(p2, q2)) {
        return 1.0;
      }
      // Do they overlap? (Are all the point differences in either direction the same sign)
      Double time = timeForCollinear(p1.x(), p2.x(), q1.x(), q2.x());
      if (time != null) {
        return time;
      }
      time = timeForCollinear(p1.y(), p2.y(), q1.y(), q2.y());
      return time;
    }

    if (denominator == 0) {
      // lines are paralell
      return null;
    }

    double u = uNumerator / denominator;
    double t = Vec2.crossProduct(Vec2.subtract(q1, p1), s) / denominator;

    return (t >= 0) && (t <= 1) && (u >= 0) && (u <= 1) ? t : null;
  }

  private static boolean allEqual(boolean... values) {
    for (int i = 1; i < values.length; i++) {
      if (values[i] != values[0]) {
        return false;
      }
    }
    return true;
  }

  private static Double timeForCollinear(double A, double B, double C, double D) {
    if (allEqual(C - A < 0, C - B < 0, D - A < 0, D - B < 0)) {
      return null;
    }
    return Math.min(Math.abs(A-C), Math.abs(A - D)) / Math.abs(B - A);
  }

  private final Vec2 mP1;
  private final Vec2 mP2;
}