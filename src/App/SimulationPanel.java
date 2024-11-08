package App;

import App.Simulation.Body.Body;
import App.Simulation.Simulation;
import App.Util.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

public class SimulationPanel extends JPanel implements Runnable{
    
    public SimulationPanel(int width, int height, int FPS) {
        mScreenWidth = width;
        mScreenHeight = height;
        mTimeStep = 1.0 / FPS;
        mTimer = new Timer();
        mSimulation = new Simulation(mTimeStep);
        mMouseHandler = new MouseHandler();
        this.setPreferredSize(new Dimension(mScreenWidth, mScreenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addMouseListener(mMouseHandler);
        this.setFocusable(true);
    }

    public void startSimulation() {
        mSimulationThread = new Thread(this);
        mSimulationThread.start();
    }

    @Override
    public void run() {
      mTimer.start();
        while(mSimulationThread != null) {
            resolveMouseEvents();
            mSimulation.update();
            repaint();
            try { TimeUnit.NANOSECONDS.sleep((long) (mTimeStep * 1e9) - mTimer.getElapsedTime()); }
            catch (InterruptedException e) { System.out.println(e.getMessage()); }
            mTimer.start();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = setupG2D(g);
        drawScene(g2d);
        g2d.dispose();
    }

    private void resolveMouseEvents() {
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

    private void drawScene(Graphics2D g2d) {
        List<Body> bodies = mSimulation.getState().getBodies();
        int i = 0;
        for(Body body : bodies) {
            g2d.fill(body.getShape());
            String message = "Body " + i + " speed: (" + (int)body.velocity().x() + ", " + (int)body.velocity().y() + ")";
            g2d.drawString(message, 5, 30 + i*15);
            ++i;
        }
    }

    
    private final int mScreenWidth;
    private final int mScreenHeight;
    private final double mTimeStep;
    private final Timer mTimer;
    private final Simulation mSimulation;
    private Thread mSimulationThread;
    private final MouseHandler mMouseHandler;
}