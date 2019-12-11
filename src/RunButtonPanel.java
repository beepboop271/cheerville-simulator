import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RunButtonPanel extends JPanel implements ActionListener {
  JButton runButton;
  JButton resetButton;
  JButton defaultSettingsButton;

  WorldManager manager;
  SliderPanel sliders;

  public RunButtonPanel(WorldManager manager, SliderPanel sliders) {
    super();

    this.manager = manager;
    this.sliders = sliders;

    this.runButton = new JButton("Run");
    this.runButton.setActionCommand("run");
    this.runButton.addActionListener(this);
    this.add(this.runButton);

    this.resetButton = new JButton("Reset World");
    this.resetButton.setActionCommand("resetWorld");
    this.resetButton.addActionListener(this);
    this.add(this.resetButton);

    this.defaultSettingsButton = new JButton("Reset Settings");
    this.defaultSettingsButton.setActionCommand("resetSettings");
    this.defaultSettingsButton.addActionListener(this);
    this.add(this.defaultSettingsButton);
    
    this.setOpaque(false);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    if (command.equals("run")) {
      this.runButton.setText("Stop");
      this.runButton.setActionCommand("stop");
      manager.setRunning(true);
    } else if (command.equals("stop")) {
      this.runButton.setText("Run");
      this.runButton.setActionCommand("run");
      manager.setRunning(false);
    } else if (command.equals("resetWorld")) {
      this.runButton.setText("Run");
      this.runButton.setActionCommand("run");
      manager.setRunning(false);
      manager.reset();
    } else if (command.equals("resetSettings")) {
      this.sliders.reset();
    }
  }
}