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
        mCollisionSolver = new CollisionSolver();
        List<Body> inititalState = new ArrayList<>();
        inititalState.add(new Particle(false, new Vec2(100, 150), Vec2.zero(), new Vec2(0, 100), 1.0, 5));
        //inititalState.add(new Particle(false, new Vec2(100, 100), Vec2.zero(), new Vec2(0, 200), 1.0, 5));
        inititalState.add(new Line(new Vec2(300, 500), 1000, 60, 2));
        mState = new SimulationState(inititalState);
    }

    public SimulationState getState() { return mState; }
    public long getLastFrameTime(){ return mFrameTime; }

    public void update() {
        mFrameTime = mTimer.getElapsedTimeAndReset(); //IN NANOSECONDS!!!

        List<Body> bodies = mState.getBodies();
        for(Body body : bodies) {
            checkForCollisions(body, bodies);
            body.update(mFrameTime * 0.000000001);
        }
    }

    private void checkForCollisions(Body body, List<Body> bodies) {
        for(Body other : bodies) {
            if(body == other) { continue; }
            if(body.intersects(other)) { body.accept(mCollisionSolver, other); }
        }
    }

    private final SimulationState mState;
    private final CollisionSolver mCollisionSolver;
    private final Timer mTimer;
    private long mFrameTime;
}