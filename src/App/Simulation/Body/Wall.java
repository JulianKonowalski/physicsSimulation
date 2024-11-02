package App.Simulation.Body;

import App.Simulation.Util.LineSegment;
import App.Simulation.Util.Vec2;

import java.util.List;

public class Wall extends StaticBody {

  public Wall(LineSegment wall) { mWall = wall; }
  public LineSegment get() { return mWall; }
  public Vec2 p1() { return mWall.p1(); }
  public Vec2 p2() { return mWall.p2(); }
  
  @Override
  public List<LineSegment> getBounds() { return List.of(mWall); }

  private final LineSegment mWall;
}
