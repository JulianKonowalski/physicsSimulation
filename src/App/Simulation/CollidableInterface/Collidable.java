package App.Simulation.CollidableInterface;

public interface Collidable {
    public void accept(CollisionVisitor visitor, Collidable other);
}
