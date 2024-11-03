package App.Simulation.CollidableInterface;

import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;

public interface CollisionVisitor {
    public void visit(Particle particle, Collidable other);
    public void visit(Line line, Collidable other);
}
