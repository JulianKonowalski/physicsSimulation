# Physics Simulation
This is a 2D physics simulation written in Java. It simulates the interaction between rigid bodies and displays the result in real time using the built-in Graphics2D library. It is constantly being developed by [JulianKonowalski](https://github.com/JulianKonowalski) and [Ezic04](https://github.com/Ezic04).

# Functionality
The app currently supports two types of bodies:
* Particles (static and dynamic)
* Lines (static)

As the name suggests, static bodies remain static throughout the simulation, no matter the forces acting on them. On the other hand, dynamic bodies have their speed and position recalculated every frame.

# Adding Objects To The Scene
Objects can be added from the **Simulation Class Constructor**. It creates a new SimulationState instance there, and an ArryaList of desired objects can be passed into its constructor. It would look like this:
```
import App.Simulation.Body.Body;
import App.Simulation.Body.Particle;
import App.Simulation.Util.Vec2;
import java.util.ArrayList;
import java.util.List;

public class Simulation {

    public Simulation() {

        //Stuff before the SmulationState initialisation
        List<Body> inititalState = new ArrayList<>();
        inititalState.add(new Particle(false, new Vec2(780, 220), new Vec2(300, 300), Vec2.zero(), 1.0, 50));
        inititalState.add(new Particle(false, new Vec2(100, 250), Vec2.zero(), Vec2.zero(), 5.0, 100));

        mState = new SimulationState(inititalState);
    }
}
``` 

# Collision Detection
Currently, the simulation detects collisions in discrete timesteps. It performs multiple checks every frame, to ensure no unhandled collisions. The drawback of this approach is the possibility of **"tunnelling"** if the particles move fast enough to travel to the other side of the obstacle between the frames. We plan to switch to **Continuous Collision Detection** in the future to avoid this problem.

# Resolving Collisions
The class responsible for resolving collisions is called CollisionSolver. It acts as a **visitor**, that is called upon collision detection by one of the collided objects. It visits both parties to determine the type of collision (for example particle-particle or particle-line) and change their parameters accordingly.
