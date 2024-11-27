package App;

import App.Simulation.Body.DynamicBody;
import App.Simulation.Body.Particle;
import App.Simulation.SimulationState;
import App.Simulation.Util.Vec2;
import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class InitializationInterface {

  public InitializationInterface() {

    mScanner = new Scanner(System.in);
  }

  public SimulationState getInitialState() {

    if (App.sOptions.isDebugging()) {
      return testState();
    }

    enum Command {
      SKIP, ADD, START, TEST
    }

    List<DynamicBody> particles = new ArrayList<>();
    mScanner.useLocale(new Locale("en", "US"));
    Command currentCommand = Command.SKIP;

    while (currentCommand != Command.START) {
      String line = mScanner.nextLine();
      String[] split = line.split(" ");

      if (split[0].equalsIgnoreCase("start")) {
        currentCommand = Command.START;
      }
      else if (split[0].equalsIgnoreCase("add")) {
        currentCommand = Command.ADD;
      }
      else if (split[0].equalsIgnoreCase("test")) {
        currentCommand = Command.TEST;
      }
      else {
        System.out.println("Invalid command");
      }

      switch (currentCommand) {
        case START, SKIP:
          break;
        case TEST:
          return testState();
        case ADD:
          Particle toAdd = scanParticle(split);
          if (toAdd != null) {
            particles.add(toAdd);
          }
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + currentCommand);
      }
    }
    return SimulationState.defaultBox(particles, App.sOptions.getWidth(), App.sOptions.getHeight());
  }


  private static Particle scanParticle(String[] split) {
    String errorMessage = "Invalid input, please enter a particle in the following format: x y vx vy r";
    if(split.length == 1 || split[1].equalsIgnoreCase("help")) {
      System.out.println("Enter a particle in the following format: x y vx vy r");
      return null;
    }
    if(split.length != 6) {
      System.out.println(errorMessage);
      return null;
    }
    double[] val = new double[5];
    try {
      for (int i = 0; i < 5; i++) {
        val[i] = Double.parseDouble(split[i + 1]);
      }
    } catch (NumberFormatException e) {
      System.out.println(errorMessage);
      return null;
    }
    System.out.println("Particle added: p=(" + val[0] + ", " + val[1] + ") v=(" + val[2] + ", " + val[3] + ") r=" + val[4]);
    return new Particle(new Vec2(val[0], val[1]), new Vec2(val[2], val[3]), val[4]);
  }

  private SimulationState testState(){
    List<DynamicBody> particles = new ArrayList<>();

//    particles.add(new Particle(new Vec2(200, 100), new Vec2(-100, -300),40));
//    particles.add(new Particle(new Vec2(200, 200), new Vec2(-200, -400),40));

//    particles.add(new Particle(new Vec2(110, 100), new Vec2(-100, -100),10));
//    particles.add(new Particle(new Vec2(210, 200), new Vec2(-200, -200),32));

    particles.add(new Particle(new Vec2(100, 100), new Vec2(200, 400), 12));
    particles.add(new Particle(new Vec2(200, 200), new Vec2(-200, 400), 16));
    particles.add(new Particle(new Vec2(300, 300), new Vec2(400, 200), 24));
    particles.add(new Particle(new Vec2(400, 400), new Vec2(-400, -200), 32));
    particles.add(new Particle(new Vec2(1000, 100), new Vec2(200, 400), 12));
    particles.add(new Particle(new Vec2(1000, 200), new Vec2(-200, 400), 16));
    particles.add(new Particle(new Vec2(1000, 300), new Vec2(400, 200), 24));
    particles.add(new Particle(new Vec2(500, 400), new Vec2(-400, -200), 32));
    particles.add(new Particle(new Vec2(500, 100), new Vec2(200, 400), 12));
    particles.add(new Particle(new Vec2(500, 200), new Vec2(-200, 400), 16));
    particles.add(new Particle(new Vec2(500, 300), new Vec2(400, 200), 24));
    particles.add(new Particle(new Vec2(500, 400), new Vec2(-400, -200), 32));

    return SimulationState.defaultBox(particles, App.sOptions.getWidth(), App.sOptions.getHeight());
  }

  private final Scanner mScanner;
}
