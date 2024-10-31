package App;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener{

    @Override
    public void mouseClicked(MouseEvent e) {
        mMouseClicked = true;
        mLastMouseEvent = e;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //ignore
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //ignore
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //ignore
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //ignore
    }
    
    public boolean mouseClicked() {
        return mMouseClicked;
    }

    public MouseEvent getLastEvent() {
        mMouseClicked = false;
        return mLastMouseEvent;
    }

    private MouseEvent mLastMouseEvent;
    private boolean mMouseClicked = false;
}
