package App;

import javax.swing.JFrame;

public class Window extends JFrame {
        public SimulationPanel setup(String title, int width, int height) {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);
            this.setTitle(title);     
            SimulationPanel appPanel = new SimulationPanel(width, height);
            this.add(appPanel);
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
            return appPanel;
        }
}
