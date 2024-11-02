package App;

import App.Simulation.Body.Body;
import App.Simulation.Simulation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;

public class SimulationPanel extends JPanel implements Runnable{
    
    public SimulationPanel(int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;
        mSimulation = new Simulation();
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
        while(mSimulationThread != null) {
            resolveMouseEvents();
            mSimulation.update();
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = setupG2D(g);

        drawScene(g2d);
        printDebugInfo(g2d);

        g2d.dispose();
    }

    private void resolveMouseEvents() {
        if (mMouseHandler.mouseClicked()) {
            MouseEvent event = mMouseHandler.getLastEvent();
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
        for(Body body : bodies) {
            g2d.fill(body.getShape());
        }
    }

    private void printDebugInfo(Graphics2D g2d) {
        //logic
    }
    
    private final int mScreenWidth;
    private final int mScreenHeight;

    private final Simulation mSimulation;
    private Thread mSimulationThread;
    private final MouseHandler mMouseHandler;
}