package App.Simulation.Body;

import App.Simulation.CollisionSolver;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class Line extends LineSegment implements Body {

  public Line(Vec2 P1, Vec2 P2, double thickness) {
    super(P1, P2);
    mShape = constructShape(thickness);
  }

  @Override
  public boolean isDynamic() { return false; }
  @Override
  public BodyType type() { return BodyType.LINE; }
  @Override
  public Vec2 position() { return Vec2.zero(); }
  @Override
  public Vec2 velocity() { return Vec2.zero(); }
  @Override
  public double mass() { return 0.0; }
  @Override
  public Vec2 predictedPosition(double t) { return Vec2.zero(); }
  @Override
  public LineSegment getLineSegment(double t) { return this; }
  @Override
  public Shape getShape() { return mShape; }
  @Override
  public void accept(CollisionSolver solver, Body other) { /* doNothing */ }

  private Path2D constructShape(double thickness) {
    Path2D path = new Path2D.Double();
    List<Line2D> lines = new ArrayList<>();
    Vec2 lineVector = new Vec2(p2().x() - p1().x(), p2().y() - p1().y());
    Vec2 perpendicularVector = Vec2.scale(Vec2.normalize(Vec2.perpendicularClockwise(lineVector)), thickness / 2);
    Vec2 point1 = Vec2.add(p1(), perpendicularVector);
    Vec2 point2 = Vec2.add(p2(), perpendicularVector);
    Vec2 point3 = Vec2.subtract(p2(), perpendicularVector);
    Vec2 point4 = Vec2.subtract(p1(), perpendicularVector);
    lines.add(new Line2D.Double(point1.x(), point1.y(), point2.x(), point2.y()));
    lines.add(new Line2D.Double(point2.x(), point2.y(), point3.x(), point3.y()));
    lines.add(new Line2D.Double(point3.x(), point3.y(), point4.x(), point4.y()));
    lines.add(new Line2D.Double(point4.x(), point4.y(), point1.x(), point1.y()));
    for(Line2D line : lines) { path.append(line, path.getCurrentPoint() != null); }
    path.closePath();
    return path;
  }

  private final Path2D mShape;
}