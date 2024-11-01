package App.Simulation.Util;

public class LineSegment  {

    public LineSegment(Vec2 p1, Vec2 p2) {
        mP1 = p1;
        mP2 = p2;
    }

    public Vec2 p1() {
        return mP1;
    }

    public Vec2 p2() {
        return mP2;
    }

    public static boolean equals(LineSegment l1, LineSegment l2) {
        return (Vec2.equals(l1.p1(), l2.p1()) && Vec2.equals(l1.p2(), l2.p2())) ||
                (Vec2.equals(l1.p1(), l2.p2()) && Vec2.equals(l1.p2(), l2.p1()));
    }
    public static Vec2 intersection(LineSegment l1, LineSegment l2) {
        Vec2 p1 = l1.p1();
        Vec2 p2 = l1.p2();
        Vec2 p3 = l2.p1();
        Vec2 p4 = l2.p2();

        float x1 = p1.x();
        float y1 = p1.y();
        float x2 = p2.x();
        float y2 = p2.y();
        float x3 = p3.x();
        float y3 = p3.y();
        float x4 = p4.x();
        float y4 = p4.y();

        float d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
        if (d == 0) {
            return null;
        }

        float x = ((x1*y2 - y1*x2)*(x3-x4) - (x1-x2)*(x3*y4 - y3*x4)) / ((x1-x2)*(y3-y4) - (y1-y2)*(x3-x4));
        float y = ((x1*y2 - y1*x2)*(y3-y4) - (y1-y2)*(x3*y4 - y3*x4)) / ((x1-x2)*(y3-y4) - (y1-y2)*(x3-x4));

        return new Vec2(x, y);
    }

    private final Vec2 mP1;
    private final Vec2 mP2;
}