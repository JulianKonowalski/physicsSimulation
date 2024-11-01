package App.Simulation;

import java.util.List;
import java.util.ArrayList;

import App.Simulation.Body.Body;
import App.Simulation.Body.DynamicBody;
import App.Simulation.Body.StaticBody;
import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;

public class Simulation {

  public Simulation(SimulationState state, int FPS) {
    timeStep = 1.0f / FPS;
    mState = state;
  }

  public void update() {
    List<LineSegment> linesToCheck = new ArrayList<>(); // TODO: para linia-obiekt
    getLinesToCheck(linesToCheck);
    //TODO: dla każdej pary obiektów sprawdź czy się przecinają
  }

  private void getLinesToCheck(List<LineSegment> linesToCheck) {
    for (DynamicBody body1 : mState.dynamicBodies()) {
      for (DynamicBody body2 : mState.dynamicBodies()) {
        if (body1 == body2) {
          continue;
        }

        Vec2 intersection = LineSegment.intersection(body1.displacement(), body2.displacement());
        if (intersection != null) {
          handleCollision(body1, body2, intersection);
        }
      }
    }
    for (StaticBody body : mState.staticBodies()) {
      linesToCheck.addAll(body.getBounds());
    }
  }

  private void handleCollision(Body body1, Body body2, Vec2 intersection) {
  }

  private SimulationState mState;
  private final float timeStep; // Time step in seconds
}
