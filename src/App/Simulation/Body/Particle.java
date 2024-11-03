package App.Simulation.Body;

import App.Simulation.Util.Vec2;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Particle extends Body {

    public Particle(boolean isStatic, Vec2 position, Vec2 velocity, Vec2 acceleration, double mass, double radius) {
        super(isStatic, position, velocity, acceleration, mass);
        if (radius < 0) { throw new IllegalArgumentException("Tried to set a negative particle radius"); }
        mVertices.add(new Vec2(0.0, 0.0));
        mRadius = radius;
    }

    public double radius() { return mRadius; }

    @Override
    public int type() { return PARTICLE; }

    @Override
    public boolean intersects(Body body) {
        switch(body.type()) {
            case LINE -> { return intersectsWithLine((Line)body); }
            case PARTICLE -> { return intersectsWithParticle((Particle)body); }
            default -> { return false; }
        }
    }

    @Override
    public void update(double timeStep) { //in seconds
        mVelocity.add(Vec2.scale(mAcceleration, timeStep));
        mPosition.add(Vec2.scale(mVelocity, timeStep));
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(
        this.position().x() - this.radius(), 
        this.position().y() - this.radius(), 
        2.0 * this.radius(), 
        2.0 * this.radius()
        );
    }

    private boolean intersectsWithLine(Line line) {
        if(Vec2.distance(mPosition, line.start()) <= mRadius) { return true; }
        else if(Vec2.distance(mPosition, line.end()) <= mRadius) { return true; }

        double dotProduct = (
                (mPosition.x() - line.start().x()) * (line.end().x() - line.start().x()) + (mPosition.y() - line.start().y()) * (line.end().y() - line.start().y())
            ) / (
                line.length() * line.length()
            );

        Vec2 closestPoint = new Vec2(
            line.start().x() + (dotProduct * (line.end().x() - line.start().x())),
            line.start().y() + (dotProduct * (line.end().y() - line.start().y()))
        );

        return line.includesPoint(closestPoint) && Vec2.distance(mPosition, closestPoint) <= mRadius;
    }

    private boolean intersectsWithParticle(Particle particle) {
        return Vec2.distance(mPosition, particle.position()) <= mRadius + particle.radius();
    }

    /* MEMBER VARIABLES */
    private final double mRadius;
}