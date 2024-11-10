package App.Simulation;

import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.Util.FutureCollisionData;
import App.Simulation.Util.Vec2;
import java.util.TreeSet;

public class CollisionSolver {

  public void resolveCollision(TreeSet<FutureCollisionData> q) {
    FutureCollisionData currentData = q.first();
    switch (currentData.collider().type()) {
        case PARTICLE -> { particleParticleCollision(currentData.body(), (Particle)currentData.collider()); }
        case LINE -> { particleLineCollision(currentData); }
        default -> {/* doNothing */}
    }
    q.remove(currentData);
  }

  private void particleParticleCollision(Particle particle1, Particle particle2) { //TODO: implement particle-particle collisions

    // Vec2 normalCollisionAxis = Vec2.normalize(Vec2.subtract(particle2.position(), particle1.position()));
    // double normalVelocity1 = Vec2.length(Vec2.project(particle1.velocity(), normalCollisionAxis));
    // double particleOverlap = particle1.radius() + particle2.radius() - Vec2.distance(particle1.position(), particle2.position());

    // if(particle2.isStatic()) { //particle1 can NEVER be static, otherwise it wouldn't have called this object
    //     particle1.setVelocity(Vec2.add(particle1.velocity(), Vec2.scale(normalCollisionAxis, normalVelocity1 * -2.0)));
    //     particle1.setPosition(Vec2.add(particle1.position(), Vec2.scale(normalCollisionAxis, particleOverlap * -1.0)));
    //     return;
    // }
    // double normalVelocity2 =Vec2.length(Vec2.project(particle2.velocity(), normalCollisionAxis));

    // double finalVelocity1 = (particle1.mass() - particle2.mass()) * normalVelocity1 + 2 * particle2.mass() * normalVelocity2;
    // finalVelocity1 /= particle1.mass() + particle2.mass();
    // particle1.setVelocity(Vec2.add(particle1.velocity(), Vec2.scale(normalCollisionAxis, finalVelocity1 * -2.0)));

    // double finalVelocity2 = (particle2.mass() - particle1.mass()) * normalVelocity2 + 2 * particle1.mass() * normalVelocity1;
    // finalVelocity2 /= particle1.mass() + particle2.mass();
    // particle2.setVelocity(Vec2.add(particle2.velocity(), Vec2.scale(normalCollisionAxis, finalVelocity2 * -2.0)));

    // particle1.setPosition(Vec2.add(particle1.position(), Vec2.scale(normalCollisionAxis, particleOverlap / -2))); //check if this is correct, because idk
    // particle1.setPosition(Vec2.add(particle1.position(), Vec2.scale(normalCollisionAxis, particleOverlap / 2)));

  }

  private void particleLineCollision(FutureCollisionData data) {

    Particle particle = data.body();
    Line line = (Line)data.collider();
    double timeToCollision = data.timeToCollision();

    particle.updatePosition(timeToCollision);
    Vec2 normalCollisionVector = new Vec2(line.p1().y() - line.p2().y(), line.p2().x() - line.p1().x());
    if (Vec2.dotProduct(particle.velocity(), normalCollisionVector) > 0) { normalCollisionVector.negate(); }
    particle.setVelocity(solverKey, Vec2.subtract(particle.velocity(), Vec2.scale(normalCollisionVector, 2 * Vec2.dotProduct(particle.velocity(), normalCollisionVector) / Vec2.lengthSquared(normalCollisionVector))));
    particle.addToInternalTime(solverKey, timeToCollision);
    
  }

  public static final class SolverKey { private SolverKey(){}; }
  private static final SolverKey solverKey = new SolverKey();

}
