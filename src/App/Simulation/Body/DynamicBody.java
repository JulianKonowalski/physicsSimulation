package App.Simulation.Body;

import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;

public abstract class DynamicBody implements Body {

    public DynamicBody(Vec2 position, Vec2 velocity, Vec2 acceleration, float mass, float timeStep) {
        mPosition = position;
        mVelocity = velocity;
        mAcceleration = acceleration;
        mMass = mass;
        calculateFuturePath(timeStep);
    }

    protected void calculateFuturePath(float timeStep) {
        mDisplacement =  new LineSegment(position(), Vec2.add(position(), Vec2.scale(velocity(), timeStep)));
    }

    public Vec2 position() { return mPosition; }
    public Vec2 velocity() { return mVelocity; }
    public Vec2 acceleration() { return mAcceleration; }
    public float mass() { return mMass; }
    public LineSegment displacement() { return mDisplacement; }

    public void setPosition(Vec2 position) { mPosition = position; }
    public void setVelocity(Vec2 velocity) { mVelocity = velocity; }
    public void setAcceleration(Vec2 acceleration) { mAcceleration = acceleration; }

    protected Vec2 mPosition;
    protected Vec2 mVelocity;
    protected Vec2 mAcceleration;
    protected final float mMass;
    protected LineSegment mDisplacement;
}