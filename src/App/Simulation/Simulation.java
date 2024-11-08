package App.Simulation;

import App.Simulation.Body.Body;
import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.Util.Vec2;
import java.util.ArrayList;
import java.util.List;

public class Simulation {

    public Simulation(double timeStep) {
        mTimeStep = timeStep;
        mCollisionSolver = new CollisionSolver();
        List<Body> inititalState = new ArrayList<>();
        inititalState.add(new Particle(false, new Vec2(780, 220), new Vec2(300, 300), Vec2.zero(), 1.0, 50));
        //inititalState.add(new Particle(false, new Vec2(100, 250), Vec2.zero(), Vec2.zero(), 5.0, 100));

        inititalState.add(new Line(new Vec2(1280 / 2, 720), 1280, 0, 2));
        inititalState.add(new Line(new Vec2(1280 / 2, 0), 1280, 180, 2));
        inititalState.add(new Line(new Vec2(0, 720 / 2), 720, 90, 2));
        inititalState.add(new Line(new Vec2(1280, 720 / 2), 720, 270, 2));

        mState = new SimulationState(inititalState);
    }

    public SimulationState getState() { return mState; }

    public void update() {
        List<Body> bodies = mState.getBodies();
        for(Body body : bodies) {
            checkForCollisions(body, bodies);
            body.update(mTimeStep);
        }
    }

    private void checkForCollisions(Body body, List<Body> bodies) {
        for(Body other : bodies) {
            if(other == body) { continue; }
            if(body.intersects(other)) { body.accept(mCollisionSolver, other); }
        }
    }

    private final SimulationState mState;
    private final CollisionSolver mCollisionSolver;
    private final double mTimeStep;
}