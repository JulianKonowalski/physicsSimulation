package App.Simulation;

import java.util.ArrayList;
import java.util.List;
import App.Simulation.Body.*;

public record SimulationState( List<StaticBody> StaticBodies, List<DynamicBody> DynamicBodies) {

  public List<Body> Bodies() {
    List<Body> bodies = new ArrayList<>();
    bodies.addAll(DynamicBodies);
    bodies.addAll(StaticBodies);
    return bodies;
  }
}