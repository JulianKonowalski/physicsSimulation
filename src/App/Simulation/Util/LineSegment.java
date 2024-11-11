package App.Simulation.Util;

public class LineSegment {
  public LineSegment(Vec2 P1, Vec2 P2) {
    mP1 = P1;
    mP2 = P2;
  }

  public Vec2 p1() { return mP1; }

  public Vec2 p2() { return mP2; }

  public  static LineSegment move(LineSegment line, Vec2 displacement) {
    return new LineSegment(Vec2.add(line.p1(), displacement), Vec2.add(line.p2(), displacement));
  }

  private final Vec2 mP1;
  private final Vec2 mP2;
}