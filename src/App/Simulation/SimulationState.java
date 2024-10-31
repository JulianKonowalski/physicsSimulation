package App.Simulation;

import java.util.List;

import App.Simulation.Body.LineSegment;
import App.Simulation.Body.Particle;

public class SimulationState {

    public SimulationState(List<LineSegment> walls, List<Particle> particles) {
        mWalls = walls;
        mParticles = particles;
    }

    //public so it can be easily accessed; validation of data is not possible anyway
    public List<LineSegment> mWalls;
    public List<Particle> mParticles;
}
