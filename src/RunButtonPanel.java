import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class RunButtonPanel extends JPanel implements ActionListener {
  JButton runButton;
  JButton stopButton;
  JButton resetButton;

  WorldManager manager;

  public RunButtonPanel(WorldManager manager) {
    super();

    this.manager = manager;

    this.runButton = new JButton("Run");
    this.runButton.setActionCommand("run");
    this.runButton.addActionListener(this);
    this.add(this.runButton);

    this.resetButton = new JButton("Reset");
    this.resetButton.setActionCommand("reset");
    this.resetButton.addActionListener(this);
    this.add(this.resetButton);
    
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
    } else if (command.equals("reset")) {
      this.runButton.setText("Run");
      this.runButton.setActionCommand("run");
      manager.setRunning(false);
      manager.getWorldToRun().reset(WorldManager.getInitialHumans(),
                                    WorldManager.getInitialZombies());

    }
  }
}