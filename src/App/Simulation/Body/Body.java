package App.Simulation.Body;

import App.Simulation.Util.LineSegment;

public interface Body {

  Type type();
  LineSegment getLineSegment(double t);

  enum Type {
    PARTICLE,
    LINE,
  }
}