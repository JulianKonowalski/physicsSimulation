package App.Graphics;

import App.Simulation.Util.Vec2;
import java.awt.Shape;

public interface GraphicsShape {
  public Shape getShape();
  public void update(Vec2 position);
}
