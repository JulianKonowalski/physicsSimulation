package App.Simulation;

import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.CollidableInterface.Collidable;
import App.Simulation.CollidableInterface.CollisionVisitor;

public class CollisionSolver implements  CollisionVisitor {

    @Override
    public void visit(Particle particle, Collidable other) {
        switch(other) {
            case Line line -> { particleLineCollision(particle, line); }
            case Particle particle2 -> { particleParticleCollision(particle, particle2); }
            default->{/* doNothing */}
        }
    }

    @Override
    public void visit(Line line, Collidable other) {
        switch(other) {
            case Particle particle -> { particleLineCollision(particle, line); }
            default->{/* doNothing */}
        }
    }

    private void particleParticleCollision(Particle particle1, Particle particle2) {
        System.out.println("Particle-Particle Collision");
    }

    private void particleLineCollision(Particle particle, Line line) {
        System.out.println("Particle-Line Collision");
    }

}
