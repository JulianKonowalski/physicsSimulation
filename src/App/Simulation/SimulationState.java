package App.Simulation;

import java.util.ArrayList;
import java.util.List;
import App.Simulation.Body.*;
import App.Simulation.Util.Vec2;

public record SimulationState( List<StaticBody> StaticBodies, List<DynamicBody> DynamicBodies) {

  public List<Body> Bodies() {
    List<Body> bodies = new ArrayList<>();
    bodies.addAll(DynamicBodies);
    bodies.addAll(StaticBodies);
    return bodies;
  }

  public static SimulationState defaultBox(List<DynamicBody> particles, double width, double height) {
    List<StaticBody> walls = new ArrayList<>();
    walls.add(new Line(new Vec2(0, 0), new Vec2(width, 0), 2.0));
    walls.add(new Line(new Vec2(width, 0), new Vec2(width, height), 2.0));
    walls.add(new Line(new Vec2(width, height), new Vec2(0, height), 2.0));
    walls.add(new Line(new Vec2(0, height), new Vec2(0, 0), 2.0));
    return new SimulationState(walls, particles);
  }
}