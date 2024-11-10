package App.Simulation.Body;

import App.Simulation.Body.BodyType;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;

import java.awt.Shape;

public interface NewBody {
  public boolean isDynamic();

  public BodyType type();

  public Vec2 position();

  public Vec2 velocity();

  public double mass();

  public Vec2 predictedPosition(double t);

  public Shape getShape();

  public LineSegment getLineSegment(double t);
}
