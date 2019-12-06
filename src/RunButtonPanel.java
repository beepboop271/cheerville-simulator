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

  public RunButtonPanel() {

    this.runButton = new JButton("Run");
    this.runButton.setActionCommand("run");
    this.runButton.addActionListener(this);
    this.add(this.runButton);

    this.stopButton = new JButton("Stop");
    this.stopButton.setActionCommand("stop");
    this.stopButton.addActionListener(this);
    this.stopButton.setEnabled(false);
    this.add(this.stopButton);

    this.resetButton = new JButton("Reset");
    this.resetButton.setActionCommand("reset");
    this.resetButton.addActionListener(this);
    this.add(this.resetButton);
    
    this.setPreferredSize(new Dimension(100, 100));
    this.setMinimumSize(new Dimension(100, 100));
    this.setOpaque(false);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    if (command.equals("run")) {
      this.runButton.setEnabled(false);
      this.stopButton.setEnabled(true);


    } else if (command.equals("stop")) {
      this.stopButton.setEnabled(false);
      this.runButton.setEnabled(true);


    } else if (command.equals("reset")) {
      this.stopButton.setEnabled(false);
      this.runButton.setEnabled(true);


    }
  }
}