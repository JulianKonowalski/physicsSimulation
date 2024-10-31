package App.Simulation.Vector;

public class Vec2 {
    
    public Vec2(double x, double y) { mX = x; mY = y; }

    public double x() { return mX; }
    public double y() { return mY; }

    //operations on this instance
    public void add(Vec2 other) { mX += other.x(); mY += other.y(); }
    public void subtract(Vec2 other) {  mX -= other.x(); mY -= other.y(); }
    public void multiply(double scalar) { mX *= scalar; mY *= scalar; }
    public void divide(double scalar) { mX /= scalar; mY /= scalar; }

    //operations without modyfing this instance
    public static Vec2 add(Vec2 v1, Vec2 v2) { return new Vec2(v1.x() + v2.x(), v1.y() + v2.y()); }
    public static Vec2 subtract(Vec2 v1, Vec2 v2) { return new Vec2(v1.x() - v2.x(), v1.y() - v2.y()); }
    public static Vec2 multiply(Vec2 v1, double scalar) { return new Vec2(v1.x() * scalar, v1.y() * scalar); }
    public static Vec2 divide(Vec2 v1, double scalar) { return new Vec2(v1.x() / scalar, v1.y() / scalar); }

    public void invertX() { mX = -mX; }
    public void invertY() { mY = -mY; }

    private double mX;
    private double mY;
}
