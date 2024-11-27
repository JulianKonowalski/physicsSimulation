package App.Simulation.Util;

import java.util.List;
import App.Simulation.Body.Body;
import App.Simulation.Body.DynamicBody;

public record FutureCollisionData(DynamicBody body, double timeOfCollision, Body collider, List<Body> toRemove) implements Comparable<FutureCollisionData> {

  @Override
  public int compareTo(FutureCollisionData o) {
    int doubleCompare =  Double.compare(timeOfCollision, o.timeOfCollision);
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