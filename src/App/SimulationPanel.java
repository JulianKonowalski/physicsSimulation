package App;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

public class SimulationPanel extends JPanel implements Runnable{
    
    public SimulationPanel(int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;
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
            //simulation.update();
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = setupG2D(g);

        drawScene(g2d);
        printFrameTime(g2d); //for debugging

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
        //toDo
    }

    private void printFrameTime(Graphics2D g2d) {
        //toDo
    }
    
    private int mScreenWidth;
    private int mScreenHeight;
    private int mFPS;

    private Thread mSimulationThread;
    private MouseHandler mMouseHandler = new MouseHandler();
}