package App.Graphics;

import App.Simulation.Util.Vec2;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class GraphicsBall implements GraphicsShape {
  
  public GraphicsBall(Vec2 position, double radius) {
    mRadius = radius;
    mPosition = position;
    constructShape();
  }

  @Override
  public void update(Vec2 position) {
    mPosition = position;
    constructShape();
  }

  @Override
  public Shape getShape() { return mShape; }

  private void constructShape() {
    mShape = new Ellipse2D.Double(
      mPosition.x() - mRadius,
      mPosition.y() - mRadius,
      2.0 * mRadius,
      2.0 * mRadius);
  }

  private final double mRadius;
  protected Vec2 mPosition;
  private Shape mShape;
}
