package App.Simulation;

import App.Simulation.Body.Body;
import App.Simulation.Body.DynamicBody;
import App.Simulation.Body.StaticBody;
import App.Simulation.Util.LineSegment;

import java.util.ArrayList;
import java.util.List;

public class SimulationState {

  public SimulationState() {
    mStaticBodies = new ArrayList<>();
    mDynamicBodies = new ArrayList<>();
  }

  public SimulationState(List<StaticBody> staticBodies, List<DynamicBody> dynamicBodies) {
    mStaticBodies = staticBodies;
    mDynamicBodies = dynamicBodies;
  }

  public List<StaticBody> staticBodies() {
    return mStaticBodies;
  }

  public List<DynamicBody> dynamicBodies() {
    return mDynamicBodies;
  }

  private List<StaticBody> mStaticBodies;
  private List<DynamicBody> mDynamicBodies;
}