import App.App;

class Main {
  public static void main(String[] args) {
    App app = new App("Simulation Demo", 1280, 720, 30, "physicsSimulation.log", "yyyy-MM-dd HH:mm:ss");
    app.run(); 
  }
}