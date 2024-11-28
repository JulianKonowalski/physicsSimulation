package App;

import App.Graphics.AnimationState;
import App.Graphics.GraphicsShape;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;

public class SimulationPanel extends JPanel {

  public SimulationPanel() {
    mMouseHandler = new MouseHandler();
    this.setPreferredSize(new Dimension(App.sOptions.getWidth(), App.sOptions.getHeight()));
    this.setBackground(Color.black);
    this.setDoubleBuffered(true);
    this.addMouseListener(mMouseHandler);
    this.setFocusable(true);
  }

  public void drawScene(AnimationState animationState) { //what client calls
    mAnimationState = animationState;
    repaint(); //internal method, calls paintComponent
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = setupG2D(g);
    draw(g2d); //this does the drawing
    g2d.dispose();
  }

  public void resolveMouseEvents() {
    if (mMouseHandler.mouseClicked()) {
      MouseEvent event = mMouseHandler.getLastEvent();
      event.getX();
      event.getY();
      //logic
    }
  }

  private Graphics2D setupG2D(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(Color.white);
    return g2d;
  }

  private void draw(Graphics2D g2d) {
    if(mAnimationState == null) { return; }

    /* STATIC SHAPES */
    List<GraphicsShape> shapes = mAnimationState.getStaticShapes();
    for(GraphicsShape shape : shapes) {
      g2d.fill(shape.getShape());
    }

    /* DYNAMIC SHAPES */
    //int i = 0;
    shapes = mAnimationState.getDynamicShapes();
    for (GraphicsShape shape : shapes) {
      g2d.fill(shape.getShape());
      // String message = "Body " + i + " speed: (" + (int) dynamicBody.velocity().x() + ", " + (int) dynamicBody.velocity().y() + ")";
      // g2d.drawString(message, 5, 30 + i * 15);
      // ++i;
    }
  }

  private AnimationState mAnimationState;
  private final MouseHandler mMouseHandler;
}