package App.Simulation.Body;

import App.Simulation.Util.LineSegment;

public interface StaticBody extends Body {

  @Override
  Type type();
  @Override
  LineSegment getLineSegment(double t);

}
