package App.Simulation.Body;

import java.util.List;
import App.Simulation.Util.LineSegment;

public abstract class StaticBody implements Body {
    @Override
    public void update(float timeStep) {}
    public abstract List<LineSegment> getBounds();
}
