package App.Simulation.Body;

import App.Simulation.Vector.Vec2;

public class DynamicBody {

    public DynamicBody(Vec2 position, Vec2 velocity, Vec2 acceleration, float mass) {
        mPosition = position;
        mVelocity = velocity;
        mAcceleration = acceleration;
        mMass = mass;
    }
    
    public Vec2 position() { return mPosition; }
    public Vec2 velocity() { return mVelocity; }
    public Vec2 acceleration() { return mAcceleration; }
    public float mass() { return mMass; }

    public void setPosition(Vec2 position) { mPosition = position; }
    public void setVelocity(Vec2 velocity) { mVelocity = velocity; }
    public void setAcceleration(Vec2 acceleration) { mAcceleration = acceleration; }

    private Vec2 mPosition;
    private Vec2 mVelocity;
    private Vec2 mAcceleration;
    private final float mMass;
}