package App.Graphics;

import App.Simulation.Body.DynamicBody;
import App.Simulation.Body.Line;
import App.Simulation.Body.Particle;
import App.Simulation.Body.StaticBody;
import App.Simulation.SimulationState;
import App.Simulation.Util.Vec2;
import java.util.ArrayList;
import java.util.List;

public class AnimationConverter {
  
  public List<GraphicsShape> getDynamicShapes(SimulationState simulationState) {
    List<GraphicsShape> shapes = new ArrayList<>();
    List<DynamicBody> bodies = simulationState.DynamicBodies(); 
    for(DynamicBody body : bodies) {
      if(body instanceof Particle particle) {
        shapes.add(new GraphicsBall(particle.position(), particle.radius()));
      }
    }
    return shapes;
  }

  public List<GraphicsShape> getStaticShapes(SimulationState simulationState) {
    List<GraphicsShape> shapes = new ArrayList<>();
    List<StaticBody> bodies = simulationState.StaticBodies(); 
    for(StaticBody body : bodies) {
      if(body instanceof Line line) {
        shapes.add(new GraphicsLine(line.p1(), line.p2(), line.thickness()));
      }
    }
    return shapes;
  }

  public void updateAnimationState(AnimationState animationState, SimulationState simulationState) {
    List<GraphicsShape> shapes = animationState.getDynamicShapes();
    List<DynamicBody> bodies = simulationState.DynamicBodies();
    for(int i = 0; i < shapes.size(); ++i) {
      shapes.get(i).update(bodies.get(i).position());
    }
  }

  public void updateAnimationState(AnimationState animationState, List<Vec2> simulationFrame) {
    List<GraphicsShape> shapes = animationState.getDynamicShapes();
    for(int i = 0; i < shapes.size(); ++i) {
      shapes.get(i).update(simulationFrame.get(i));
    }
  }

}
