package App;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;
import App.Simulation.Body.Body;
import App.Simulation.Body.DynamicBody;

public class SimulationPanel extends JPanel {

  public SimulationPanel(int width, int height, int FPS) {
    mScreenWidth = width;
    mScreenHeight = height;
    mMouseHandler = new MouseHandler();
    this.setPreferredSize(new Dimension(mScreenWidth, mScreenHeight));
    this.setBackground(Color.black);
    this.setDoubleBuffered(true);
    this.addMouseListener(mMouseHandler);
    this.setFocusable(true);
  }

  public void drawScene(List<Body> bodies) { //what client calls
    if(bodies == null) { return; } //it'll drop a frame
    mBodies = bodies;
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
    if(mBodies == null) { return; }
    int i = 0;
    for (Body body : mBodies) {
      g2d.fill(body.getShape());
      if (body instanceof DynamicBody dynamicBody) {
        String message = "Body " + i + " speed: (" + (int) dynamicBody.velocity().x() + ", " + (int) dynamicBody.velocity().y() + ")";
        g2d.drawString(message, 5, 30 + i * 15);
        ++i;
      }
    }
  }

  private List<Body> mBodies;  
  private final int mScreenWidth;
  private final int mScreenHeight;
  private final MouseHandler mMouseHandler;
}