package App.Simulation.Body;

import java.awt.Shape;
import App.Simulation.Util.LineSegment;

public interface Body {

  Type type();
  Shape getShape();
  LineSegment getLineSegment(double t);

  enum Type {
    PARTICLE,
    LINE,
  }
}