package App.Simulation;

import App.Simulation.Body.LineSegment;
import App.Simulation.Body.Particle;
import java.util.ArrayList;
import java.util.List;

public class SimulationState {

    public SimulationState(List<LineSegment> walls, List<Particle> particles) {
        mWalls = walls;
        mParticles = particles;
    }

    public List<LineSegment> getWalls() { return mWalls; }
    public List<Particle> getParticles() { return mParticles; }

    private List<LineSegment> mWalls = new ArrayList<>();
    private List<Particle> mParticles = new ArrayList<>();
}
