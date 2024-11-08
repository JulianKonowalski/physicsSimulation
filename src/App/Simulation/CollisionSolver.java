package App.Simulation;

import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.CollidableInterface.Collidable;
import App.Simulation.CollidableInterface.CollisionVisitor;
import App.Simulation.Util.Vec2;

public class CollisionSolver implements  CollisionVisitor {

    @Override
    public void visit(Particle particle, Collidable other) {
        switch(other) {
            case Line line -> { particleLineCollision(particle, line); }
            case Particle particle2 -> { particleParticleCollision(particle, particle2); }
            default -> {/* doNothing */}
        }
    }

    @Override
    public void visit(Line line, Collidable other) {
        switch(other) {
            case Particle particle -> { particleLineCollision(particle, line); }
            default -> {/* doNothing */}
        }
    }

    private void particleParticleCollision(Particle particle1, Particle particle2) {

        Vec2 normalCollisionAxis = Vec2.normalize(Vec2.subtract(particle2.position(), particle1.position()));
        double normalVelocity1 = Vec2.length(Vec2.project(particle1.velocity(), normalCollisionAxis));
        double particleOverlap = particle1.radius() + particle2.radius() - Vec2.distance(particle1.position(), particle2.position());

        if(particle2.isStatic()) { //particle1 can NEVER be static, otherwise it wouldn't have called this object
            particle1.setVelocity(Vec2.add(particle1.velocity(), Vec2.scale(normalCollisionAxis, normalVelocity1 * -2.0)));
            particle1.setPosition(Vec2.add(particle1.position(), Vec2.scale(normalCollisionAxis, particleOverlap * -1.0)));
            return;
        }
        double normalVelocity2 =Vec2.length(Vec2.project(particle2.velocity(), normalCollisionAxis));

        double finalVelocity1 = (particle1.mass() - particle2.mass()) * normalVelocity1 + 2 * particle2.mass() * normalVelocity2;
        finalVelocity1 /= particle1.mass() + particle2.mass();
        particle1.setVelocity(Vec2.add(particle1.velocity(), Vec2.scale(normalCollisionAxis, finalVelocity1 * -2.0)));

        double finalVelocity2 = (particle2.mass() - particle1.mass()) * normalVelocity2 + 2 * particle1.mass() * normalVelocity1;
        finalVelocity2 /= particle1.mass() + particle2.mass();
        particle2.setVelocity(Vec2.add(particle2.velocity(), Vec2.scale(normalCollisionAxis, finalVelocity2 * -2.0)));

        particle1.setPosition(Vec2.add(particle1.position(), Vec2.scale(normalCollisionAxis, particleOverlap / -2))); //check if this is correct, because idk
        particle1.setPosition(Vec2.add(particle1.position(), Vec2.scale(normalCollisionAxis, particleOverlap / 2)));
    }

    private void particleLineCollision(Particle particle, Line line) {
        Vec2 normalCollisionAxis = Vec2.normalize(Vec2.perpendicularCounterClockwise(new Vec2(line.end().x() - line.start().x(), line.end().y() - line.start().y())));
        particle.setVelocity(Vec2.subtract(particle.velocity(), Vec2.scale(normalCollisionAxis, 2 * Vec2.dotProduct(particle.velocity(), normalCollisionAxis) / Vec2.lengthSquared(normalCollisionAxis))));
    }

}
