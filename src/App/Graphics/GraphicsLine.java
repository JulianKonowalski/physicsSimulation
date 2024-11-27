package App.Graphics;

import App.Simulation.Util.Vec2;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class GraphicsLine implements GraphicsShape {

  public GraphicsLine(Vec2 p1, Vec2 p2, double thickness) {
    mP1 = p1;
    mP2 = p2;
    mShape = constructShape(thickness);
  }

  @Override
  public Shape getShape() { return mShape; }

  @Override
  public void update(Vec2 position) {/* doNothing */}

  private Path2D constructShape(double thickness) {
    Path2D path = new Path2D.Double();
    List<Line2D> lines = new ArrayList<>();
    Vec2 lineVector = new Vec2(mP2.x() - mP1.x(), mP2.y() - mP1.y());
    Vec2 perpendicularVector = Vec2.scale(Vec2.normalize(Vec2.perpendicularClockwise(lineVector)), thickness / 2);
    Vec2 point1 = Vec2.add(mP1, perpendicularVector);
    Vec2 point2 = Vec2.add(mP2, perpendicularVector);
    Vec2 point3 = Vec2.subtract(mP2, perpendicularVector);
    Vec2 point4 = Vec2.subtract(mP1, perpendicularVector);
    lines.add(new Line2D.Double(point1.x(), point1.y(), point2.x(), point2.y()));
    lines.add(new Line2D.Double(point2.x(), point2.y(), point3.x(), point3.y()));
    lines.add(new Line2D.Double(point3.x(), point3.y(), point4.x(), point4.y()));
    lines.add(new Line2D.Double(point4.x(), point4.y(), point1.x(), point1.y()));
    for (Line2D line : lines) {
      path.append(line, path.getCurrentPoint() != null);
    }
    path.closePath();
    return path;
  }

  private final Vec2 mP1;
  private final Vec2 mP2;
  private final Shape mShape;

}
