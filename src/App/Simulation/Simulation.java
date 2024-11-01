package App.Simulation;

import java.util.List;
import java.util.ArrayList;

import App.Simulation.Body.DynamicBody;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Pair;
import App.Simulation.Util.Vec2;


public class Simulation {

  public Simulation(SimulationState state, int FPS) {
    timeStep = 1.0f / FPS;
    mState = state;
  }

  public void update() {
//    List<LineSegment> linesToCheck = new ArrayList<>(mState.dynamicBodies().size());
//    List<Intersection> intersections = new ArrayList<>();
//    getLinesToCheck(linesToCheck);
//    findIntersections(linesToCheck, intersections);


  }

//  private void findIntersections(List<LineSegment> linesToCheck, List<Intersection> intersections) {
//    for (int i = 0; i < linesToCheck.size(); ++i) {
//      for (int j = i + 1; j < linesToCheck.size(); ++j) {
//        Vec2 intersection = LineSegment.intersection(linesToCheck.get(i), linesToCheck.get(j));
//        if (intersection != null) {
//          intersections.add(new Intersection(intersection, mState.dynamicBodies().get(i), mState.dynamicBodies().get(j)));
//        }
//      }
//    }
//  }

//  private void getLinesToCheck(List<LineSegment> linesToCheck) {
//    for (DynamicBody body : mState.dynamicBodies()) {
//      linesToCheck.add(new LineSegment(body.position(), body.getPositionAfterTime(timeStep)));
//    }
//  }

  private SimulationState mState;
  private final float timeStep; // Time step in seconds

//  private static class Intersection{
//    public Vec2 point;
//    public DynamicBody body1;
//    public DynamicBody body2;
//    public Intersection(Vec2 point, DynamicBody b1, DynamicBody b2) {
//      point = point;
//      body1 = b1;
//      body2 = b2;
//    }
//  }
//  private static class
}