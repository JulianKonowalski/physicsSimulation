# Physics Simulation
This is a 2D physics simulation written in Java. It simulates the interaction between rigid bodies and displays the result in real time using the built-in Graphics2D library. It is constantly being developed by [JulianKonowalski](https://github.com/JulianKonowalski) and [Ezic04](https://github.com/Ezic04).

# Functionality

## Types of bodies
The app currently supports two types of bodies:
* Particles (dynamic)
* Lines (static)

As the name suggests, static bodies remain static throughout the simulation, no matter the forces acting on them. On the other hand, dynamic bodies have their speed and position recalculated every frame.

## Collision Detection
The simulation fully utilizes **Continuous Collision Detection**, which prevents the common problem known as **tunnelling**. With each frame the simulation engine "predicts" what collisions will occur during the next frame and resolves them beforehand. The whole of collision detection logic is done by CollisionDetector class.

## Exporting the simulation
While running, the simulation is being exported to a .sim file with all the necessary data to play the simulation back. We plan to implement the playback feature in the near future.

## Logs
All the logs are exported to .log file in the main directory. The logs contain information about what happend, when it happened and which object called the log.

# Adding Objects To The Scene
Objects can be added from the **Simulation class constructor**. It creates a new SimulationState instance there, and an ArryaList of desired objects can be passed into its constructor. It would look like this:
```
import App.Simulation.Body.Body;
import App.Simulation.Body.Particle;
import App.Simulation.Util.Vec2;
import java.util.ArrayList;
import java.util.List;

public class Simulation {

    public Simulation() {

        //Stuff before the SmulationState initialisation

        List<Body> inititalState = new ArrayList<>();                                                           //create a list to be passed in
        inititalState.add(new Particle(false, new Vec2(780, 220), new Vec2(300, 300), Vec2.zero(), 1.0, 50));   //add the desired objects
        inititalState.add(new Particle(false, new Vec2(100, 250), Vec2.zero(), Vec2.zero(), 5.0, 100));

        mState = new SimulationState(inititalState);                                                            //initialise the SimulationState with the created list
    }
}
``` 
