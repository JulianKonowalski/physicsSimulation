package App.Simulation.Util;

import App.Simulation.Body.Body;
import App.Simulation.Body.Particle;

public record FutureCollisionData(Particle body, double timeToCollision, Body collider) implements Comparable<FutureCollisionData> {

  @Override
  public int compareTo(FutureCollisionData o) {
    int doubleCompare =  Double.compare(timeToCollision, o.timeToCollision);
    if (doubleCompare != 0) { return doubleCompare; } 
    else { return Integer.compare(body.hashCode(), o.body.hashCode()); }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof FutureCollisionData particleData) { return body == particleData.body; }
    return false;
  }

  @Override
  public int hashCode() { return body.hashCode(); }
}