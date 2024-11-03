package App.Simulation.Util;

public class Edge {

    public Edge(Vec2 start, Vec2 end) {
        mStart = start;
        mEnd = end;
    }

    public Vec2 start() { return mStart; }
    public Vec2 end() { return mEnd; }

    private final Vec2 mStart;
    private final Vec2 mEnd;
}
