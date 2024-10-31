package App.Simulation.Body;

import App.Simulation.Vector.Vec2;

public class DynamicBody {

    public DynamicBody(Vec2 position, Vec2 velocity, Vec2 acceleration, float mass) {
        mPosition = position;
        mVelocity = velocity;
        mAcceleration = acceleration;
        mMass = mass;
    }
    public Vec2 Pos() {
        return mPosition;
    }
    public Vec2 Vel() {
        return mVelocity;
    }
    public Vec2 Acc() {
        return mAcceleration;
    }
    public float Mass() {
        return mMass;
    }
    public void SetPos(Vec2 position) {
        mPosition = position;
    }
    public void SetVel(Vec2 velocity) {
        mVelocity = velocity;
    }
    public void SetAcc(Vec2 acceleration) {
        mAcceleration = acceleration;
    }

    private Vec2 mPosition;
    private Vec2 mVelocity;
    private Vec2 mAcceleration;
    private final float mMass;
}