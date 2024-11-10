package App.Simulation.Util;

import App.Simulation.Body.NewBody;
import App.Simulation.Body.NewParticle;

public record ParticleData(NewParticle current, double timeToCollision, NewBody other) implements Comparable<ParticleData> {

  @Override
  public int compareTo(ParticleData o) {
    int doubleCompare =  Double.compare(timeToCollision, o.timeToCollision);
    if (doubleCompare != 0) { return doubleCompare;
    } else {
      return Integer.compare(current.hashCode(), o.current.hashCode());
    }
  }

  @Override
  public boolean equals(Object obj) {
  if (obj instanceof ParticleData particleData) { return current == particleData.current; }
    return false;
  }

  @Override
  public int hashCode() { return current.hashCode(); }
}