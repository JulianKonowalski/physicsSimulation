package App.Simulation.Body;

import App.Simulation.Vector.Vec2;

public class LineSegment extends StaticBody {

    LineSegment(Vec2 p1, Vec2 p2) {
        mP1 = p1;
        mP2 = p2;
    }

    public Vec2 p1() {
        return mP1;
    }

    public Vec2 p2() {
        return mP2;
    }

    static boolean equals(LineSegment l1, LineSegment l2) {
        return (Vec2.equals(l1.p1(), l2.p1()) && Vec2.equals(l1.p2(), l2.p2())) ||
                (Vec2.equals(l1.p1(), l2.p2()) && Vec2.equals(l1.p2(), l2.p1()));
    }

    private final Vec2 mP1;
    private final Vec2 mP2;
}