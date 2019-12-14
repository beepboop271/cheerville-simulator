import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * [RunButtonPanel]
 * Holds three buttons to control the simulation. Pause/resume, reset,
 * and reset settings.
 * 2019-12-13
 * @version 1.1
 * @author Kevin Qiao
 */
public class RunButtonPanel extends JPanel implements ActionListener {
  private JButton runButton;
  private JButton resetButton;
  private JButton defaultSettingsButton;

  private CheervilleManager manager;
  private SliderPanel sliders;

  
  /** 
   * [runButtonPanel]
   * Constructor for a panel which contains JButtons to
   * control the simulation.
   * @param manager The CheervilleManager of the simulation.
   * @param sliders The JSlider panel which this can reset.
   */
  public RunButtonPanel(CheervilleManager manager, SliderPanel sliders) {
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

  
  /** 
   * [actionPerformed]
   * Method that runs when an ActionEvent is emitted.
   * Handles button presses to control the simulation.
   * @param e The ActionEvent emitted.
   */
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