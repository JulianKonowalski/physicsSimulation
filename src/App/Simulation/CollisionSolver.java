package App.Simulation;

import java.util.TreeSet;
import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.Util.FutureCollisionData;
import App.Simulation.Util.Vec2;

public class CollisionSolver {

  public void resolveCollision(TreeSet<FutureCollisionData> q) {
    //TODO: more than 1 collision at a time
    FutureCollisionData currentData = q.first();
    // assumes current is a particle
    switch (currentData.collider().type()) {
      case PARTICLE -> { particleParticleCollision(currentData); }
      case LINE -> { particleLineCollision(currentData); }
      default -> {/* doNothing */}
    }
  }

  private void particleParticleCollision(FutureCollisionData data) {

    Particle particle1 = (Particle) data.body();
    double timeOfCollision = data.timeOfCollision();
    Particle particle2 = (Particle) data.collider();

    particle1.updatePosition(timeOfCollision - particle1.timeInternal());
    particle2.updatePosition(timeOfCollision - particle2.timeInternal());

    Vec2 S = Vec2.subtract(particle1.position(), particle2.position());
    Vec2 V = Vec2.subtract(particle1.velocity(), particle2.velocity());
    double dotProduct = Vec2.dotProduct(V, S);
    double normSquared = Vec2.lengthSquared(S);
    double velocityCoefficient = dotProduct / normSquared;

    double inverseMassSum = 1.0 / (particle1.mass() + particle2.mass());
    double massCoefficient1 = 2 * particle2.mass() * inverseMassSum;
    double massCoefficient2 = 2 * particle1.mass() * inverseMassSum;

    Vec2 newParticle1Velocity = Vec2.subtract(particle1.velocity(), Vec2.scale(S, massCoefficient1 * velocityCoefficient));
    S.negate();
    Vec2 newParticle2Velocity = Vec2.subtract(particle2.velocity(), Vec2.scale(S, massCoefficient2 * velocityCoefficient));

    particle1.setVelocity(solverKey, newParticle1Velocity);
    particle2.setVelocity(solverKey, newParticle2Velocity);
  }

  private void particleLineCollision(FutureCollisionData data) {
    // assumes current is a particle
    Particle particle = (Particle) data.body();
    Line line = (Line) data.collider();
    double timeOfCollision = data.timeOfCollision();

    particle.updatePosition(timeOfCollision - particle.timeInternal());

    Vec2 normalLineVector = new Vec2(line.p1().y() - line.p2().y(), line.p2().x() - line.p1().x());
    double dotProduct = Vec2.dotProduct(particle.velocity(), normalLineVector);
    if (dotProduct > 0) {
      dotProduct *= -1;
      normalLineVector.negate();
    }

    particle.setVelocity(solverKey, Vec2.subtract(particle.velocity(), Vec2.scale(normalLineVector, 2 * dotProduct / Vec2.lengthSquared(normalLineVector))));
  }

  public static final class SolverKey { private SolverKey() {} }

  private static final SolverKey solverKey = new SolverKey();
  static boolean flag = false;
}
