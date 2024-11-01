package App.Simulation.Body;

import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;

public class Particle extends DynamicBody {

    public Particle(Vec2 position, Vec2 velocity, Vec2 acceleration, float mass, int radius, float timeStep) {
        super(position, velocity, acceleration, mass, timeStep);
        if (radius < 0) {
            throw new IllegalArgumentException("Tried to set a negative particle radius");
        }
        mRadius = radius;
    }

    @Override
    public void update(float timeStep) {
        calculateFuturePath(timeStep);
    }

    public int radius() { return mRadius; }

    private final int mRadius;
}