package App.Simulation.Body;

import java.awt.Shape;
import App.Simulation.Util.LineSegment;

public interface StaticBody extends Body {

  @Override
  Type type();
  @Override
  Shape getShape();
  @Override
  LineSegment getLineSegment(double t);

}
