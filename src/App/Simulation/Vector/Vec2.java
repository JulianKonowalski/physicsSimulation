package App.Simulation.Vector;

public class Vec2 {
    
    public Vec2(float x, float y) { mX = x; mY = y; }

    public float x() { return mX; }
    public float y() { return mY; }

    //operations on this instance
    public void set(float x, float y) { mX = x; mY = y; }
    public void add(Vec2 other) { mX += other.x(); mY += other.y(); }
    public void subtract(Vec2 other) {  mX -= other.x(); mY -= other.y(); }
    public void scale(float scalar) { mX *= scalar; mY *= scalar; }
    public float lengthSquared() { return mX * mX + mY * mY; }
    public float length() { return (float)Math.sqrt(mX * mX + mY * mY); }
    public void invert() { mX = -mX; mY = -mY; }
    public void rotate(float angle) {
        float x = mX * (float)Math.cos(angle) - mY * (float)Math.sin(angle);
        float y = mX * (float)Math.sin(angle) + mY * (float)Math.cos(angle);
        mX = x;
        mY = y;
    }
    public void normalize(){
        float len = length();
        if (len != 0) {
            mX /= len;
            mY /= len;
        }
    }
    //operations without modifying this instance
    public static Vec2 zero() { return new Vec2(0, 0); }
    public static Vec2 add(Vec2 v1, Vec2 v2) { return new Vec2(v1.x() + v2.x(), v1.y() + v2.y()); }
    public static Vec2 subtract(Vec2 v1, Vec2 v2) { return new Vec2(v1.x() - v2.x(), v1.y() - v2.y()); }
    public static Vec2 scale(Vec2 v1, float scalar) { return new Vec2(v1.x() * scalar, v1.y() * scalar); }
    public static boolean equals(Vec2 v1, Vec2 v2) { return v1.x() == v2.x() && v1.y() == v2.y(); }
    public static float innerProduct(Vec2 v1, Vec2 v2) { return v1.x() * v2.x() + v1.y() * v2.y(); }
    public static float distanceSquared(Vec2 v1, Vec2 v2) { return (v1.x() - v2.x()) * (v1.x() - v2.x()) + (v1.y() - v2.y()) * (v1.y() - v2.y()); }
    public static float distance(Vec2 v1, Vec2 v2) { return (float)Math.sqrt((v1.x() - v2.x()) * (v1.x() - v2.x()) + (v1.y() - v2.y()) * (v1.y() - v2.y())); }
    public static float angle(Vec2 v1, Vec2 v2) { return (float)Math.acos(innerProduct(v1, v2) / (v1.length() * v2.length())); }
    public static Vec2 invert(Vec2 v) { return new Vec2(-v.x(), -v.y()); }
    public static Vec2 rotate(Vec2 v, float angle) {
        float x = v.x() * (float)Math.cos(angle) - v.y() * (float)Math.sin(angle);
        float y = v.x() * (float)Math.sin(angle) + v.y() * (float)Math.cos(angle);
        return new Vec2(x, y);
    }
    public static Vec2 normalize(Vec2 v){
        float len = v.length();
        if (len != 0) {
            return new Vec2(v.x() / len, v.y() / len);
        }
        return new Vec2(0, 0);
    }

    private float mX;
    private float mY;
}
