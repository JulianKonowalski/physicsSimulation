package App.Graphics;

import java.util.ArrayList;
import java.util.List;

public class AnimationState {
  
  public AnimationState(List<GraphicsShape> staticShapes, List<GraphicsShape> dynamicShapes) {
    mStaticShapes = staticShapes;
    mDynamicShapes = dynamicShapes;
  }

  public List<GraphicsShape> getDynamicShapes() { return mDynamicShapes; }
  public List<GraphicsShape> getStaticShapes() { return mStaticShapes; }

  private List<GraphicsShape> mDynamicShapes = new ArrayList<>();
  private List<GraphicsShape> mStaticShapes = new ArrayList<>();

}
