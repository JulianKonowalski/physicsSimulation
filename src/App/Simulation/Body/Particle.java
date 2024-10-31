package App.Simulation.Body;

import App.Simulation.Vector.Vec2;

public class Particle extends  DynamicBody {

    public Particle(Vec2 position, Vec2 velocity, Vec2 acceleration, float mass, int radius) {
        super(position, velocity, acceleration, mass);
        mRadius = radius;
    }

    public Particle(Vec2 position, Vec2 velocity, Vec2 acceleration, int radius) {
        super(position, velocity, acceleration, 1);
        if (radius < 0) {
            throw new IllegalArgumentException("Radius must be non-negative");
        }
        mRadius = radius;
    }

    public Particle(Vec2 position, Vec2 velocity, int radius) {
        super(position, velocity, Vec2.zero(), 1);
        if (radius < 0) {
            throw new IllegalArgumentException("Radius must be non-negative");
        }
        mRadius = radius;
    }

    public int Radius() {
        return mRadius;
    }

    private final int mRadius;
}
