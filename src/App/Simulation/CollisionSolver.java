package App.Simulation;

import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.Util.FutureCollisionData;
import App.Simulation.Util.Vec2;

import java.util.TreeSet;

public class CollisionSolver {

  public void resolveCollision(TreeSet<FutureCollisionData> q) {
    //TODO: more than 1 collision at a time
    FutureCollisionData currentData = q.first(); //trzeba sprawdzić czy odpowiadający obiekt też ma z nim pierwsze zderzenie
    // assumes current is a particle
    switch (currentData.collider().type()) {
      case PARTICLE -> { particleParticleCollision(currentData); }
      case LINE -> { particleLineCollision(currentData); }
      default -> {/* doNothing */}
    }
    q.remove(currentData); //dodać usuwanie odpowiadającego zdarzenia
  }

  private void particleParticleCollision(FutureCollisionData data) { //TODO: implement particle-particle collisions
    flag = !flag; //to jest potrzebne bo kolizje są wykrywane dwa razy na ten moment
    if(flag) {return;}

    Particle particle1 = (Particle) data.body();
    double timeToCollision = data.timeToCollision();
    Particle particle2 = (Particle) data.collider();

    particle1.updatePosition(timeToCollision);
    particle2.updatePosition(timeToCollision);

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
    double timeToCollision = data.timeToCollision();

    particle.updatePosition(timeToCollision);

    Vec2 normalLineVector = new Vec2(line.p1().y() - line.p2().y(), line.p2().x() - line.p1().x());
    double dotProduct = Vec2.dotProduct(particle.velocity(), normalLineVector);
    if (dotProduct > 0) {
      dotProduct = -1 * dotProduct;
      normalLineVector.negate();
    }

    particle.setVelocity(solverKey, Vec2.subtract(particle.velocity(), Vec2.scale(normalLineVector, 2 * dotProduct / Vec2.lengthSquared(normalLineVector))));
  }

  public static final class SolverKey { private SolverKey() {} }

  private static final SolverKey solverKey = new SolverKey();
  static boolean flag = false;
}
