package App.Simulation.Body;

import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;

import java.awt.Shape;
import java.awt.geom.Line2D;

public class NewLine extends LineSegment implements NewBody {

  public NewLine(Vec2 P1, Vec2 P2) {
    super(P1, P2);
  }
  public Vec2 p1() { return super.p1(); }
  public Vec2 p2() { return super.p2(); }

  @Override
  public boolean isDynamic() {
    return false;
  }
  @Override
  public BodyType type() {
    return BodyType.LINE;
  }
  @Override
  public Vec2 position() {
    return Vec2.zero();
  }
  @Override
  public Vec2 velocity() {
    return Vec2.zero();
  }
  @Override
  public double mass() {
    return 0.0;
  }
  @Override
  public Vec2 predictedPosition(double t) {
    return Vec2.zero();
  }
  @Override
  public LineSegment getLineSegment(double t) {
    return this;
  }

  @Override
  public Shape getShape() {
    return new Line2D.Double(p1().x(), p1().y(), p2().x(), p2().y());
  }
}
