package App;

import javax.swing.JFrame;

public class Window extends JFrame {

  public SimulationPanel setup(String title){
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setResizable(false);
    this.setTitle(title);     
    SimulationPanel appPanel = new SimulationPanel();
    this.add(appPanel);
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    return appPanel;
  }
}
