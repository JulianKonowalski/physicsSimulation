package App.Simulation.Body;

import App.Simulation.Util.LineSegment;

import java.awt.Shape;

public interface Body {

  Type type();
  Shape getShape();
  LineSegment getLineSegment(double t);

  enum Type {
    PARTICLE,
    LINE,
  }
}