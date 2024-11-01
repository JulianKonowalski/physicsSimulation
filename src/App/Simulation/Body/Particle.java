package App.Simulation.Body;

import App.Simulation.Vector.Vec2;

public class Particle extends  DynamicBody {

    public Particle(Vec2 position, Vec2 velocity, Vec2 acceleration, float mass, int radius) {
        super(position, velocity, acceleration, mass);
        if (radius < 0) {
            throw new IllegalArgumentException("Tried to set a negative particle radius");
        }
        mRadius = radius;
    }

    public int Radius() { return mRadius; }

    private final int mRadius;
}
