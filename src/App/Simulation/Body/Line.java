package App.Simulation.Body;

import App.Simulation.Util.Vec2;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class Line extends Body {
    
    public Line(Vec2 position, double length, double angle, double thickness) {
        super(true, position, Vec2.zero(), Vec2.zero(), 0.0);
        if(length < 0.0) { throw new IllegalArgumentException("Tried to set a negative line length"); }
        if(thickness < 0.0) { throw new IllegalArgumentException("Tried to set a negative line thickness"); }
        mPosition = position;
        mLength = length;
        mAngle = angle;
        mThickness = thickness;
        double xOffset = Math.cos(angle) * length/2;
        double yOffset = Math.sin(angle) * length/2;
        mVertices.add(new Vec2(position.x() - xOffset, position.y() - yOffset)); //line start
        mVertices.add(new Vec2(position.x() + xOffset, position.y() + yOffset)); //line end
        mShape = constructShape();
    }

    @Override
    public Shape getShape() { return mShape; }

    @Override
    public void collide(Body other) {
        throw new UnsupportedOperationException("Line collisions aren't supported yet.");
    }

    @Override
    public void update(double timeStep) { /* doNothing */ }

    private Path2D constructShape() {
        Path2D path = new Path2D.Double();
        List<Line2D> lines = new ArrayList<>();
        Vec2 lineVector = new Vec2(mVertices.get(1).x() - mVertices.get(0).x(), mVertices.get(1).y() - mVertices.get(0).y());
        Vec2 perpendicularVector = Vec2.scale(Vec2.normalize(Vec2.perpendicularClockwise(lineVector)), mThickness / 2);
        Vec2 point1 = Vec2.add(mVertices.get(0), perpendicularVector);
        Vec2 point2 = Vec2.add(mVertices.get(1), perpendicularVector);
        Vec2 point3 = Vec2.subtract(mVertices.get(1), perpendicularVector);
        Vec2 point4 = Vec2.subtract(mVertices.get(0), perpendicularVector);
        lines.add(new Line2D.Double(point1.x(), point1.y(), point2.x(), point2.y()));
        lines.add(new Line2D.Double(point2.x(), point2.y(), point3.x(), point3.y()));
        lines.add(new Line2D.Double(point3.x(), point3.y(), point4.x(), point4.y()));
        lines.add(new Line2D.Double(point4.x(), point4.y(), point1.x(), point1.y()));
        for(Line2D line : lines) { path.append(line, path.getCurrentPoint() != null); }
        path.closePath();
        return path;
    }

    private final Vec2 mPosition;
    private final double mLength;
    private final double mAngle;
    private final double mThickness;
    private final Path2D mShape;

}
