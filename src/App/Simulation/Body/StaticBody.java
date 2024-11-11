package App.Simulation.Body;

import App.Simulation.Util.LineSegment;

import java.awt.*;

public interface StaticBody extends Body {

  @Override
  Type type();
  @Override
  Shape getShape();
  @Override
  LineSegment getLineSegment(double t);

}
