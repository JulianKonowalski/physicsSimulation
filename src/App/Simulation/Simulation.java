package App.Simulation;

import App.Simulation.Body.Body;
import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.Util.Timer;
import App.Simulation.Util.Vec2;
import java.util.ArrayList;
import java.util.List;

public class Simulation {

  public Simulation() {
    mTimer = new Timer(System.nanoTime());
    List<Body> inititalState = new ArrayList<>();
    inititalState.add(new Particle(false, new Vec2(100, 150), Vec2.zero(), new Vec2(0, 100), 1.0, 5));
    inititalState.add(new Particle(false, new Vec2(100, 100), Vec2.zero(), new Vec2(0, 200), 1.0, 5));
    inititalState.add(new Line(new Vec2(500, 200), 500, 60, 2));
    mState = new SimulationState(inititalState);
  }

  public SimulationState getState() { return mState; }

  public void update() {
    long timestep = mTimer.getElapsedTimeAndReset(); //IN NANOSECONDS!!!

    List<Body> bodies = mState.getBodies();
    for(Body body : bodies) {
      checkForCollisions(body, bodies);
      body.update(timestep * 0.000000001);
    }
  }

  private void checkForCollisions(Body body, List<Body> bodies) {
      for(Body other : bodies) {
        if(body == other) { continue; }

        /* collisionLogicHere */

        // Particle particle1 = (Particle)body;
        // Particle particle2 = (Particle)other;
        // if(Body.distance(body, other) <= particle1.radius() + particle2.radius()) {
        //   body.collide(other);
        // }
      }
    }

  private SimulationState mState;
  private Timer mTimer;
}