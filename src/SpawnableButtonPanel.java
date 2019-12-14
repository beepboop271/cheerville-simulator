import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * [SpawnableButtonPanel]
 * Panel which contains two buttons to select a random
 * Human or Zombie in the World.
 * 2019-12-13
 * @version 1.1
 * @author Kevin Qiao
 */
public class SpawnableButtonPanel extends JPanel implements ActionListener {
  private World worldToAccess;
  private SpawnableInfoPanel infoPanel;

  
  /** 
   * [SpawnableButtonPanel]
   * Constructor for a panel that presents two buttons
   * to select random Moveables from the World.
   * @param worldToAccess The World to select from.
   */
  public SpawnableButtonPanel(World worldToAccess) {
    this.worldToAccess = worldToAccess;

    this.setLayout(new GridBagLayout());

    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.ipadx = 10;
    c.ipady = 5;
    this.add(new JLabel("Select Random:", SwingConstants.CENTER), c);

    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 1;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.insets = new Insets(5, 0, 5, 0);
    JButton selectHumanButton = new JButton("Human");
    selectHumanButton.setActionCommand("selectHuman");
    selectHumanButton.addActionListener(this);
    this.add(selectHumanButton, c);

    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 2;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.insets = new Insets(5, 0, 5, 0);
    JButton selectZombieButton = new JButton("Zombie");
    selectZombieButton.setActionCommand("selectZombie");
    selectZombieButton.addActionListener(this);
    this.add(selectZombieButton, c);
    
    this.setPreferredSize(new Dimension(100, 100));
    this.setMinimumSize(new Dimension(100, 100));
    this.setOpaque(false);
  }

  
  /** 
   * [actionPerformed]
   * Runs whenever an action occurs. Controls the SpawnableInfoPanel
   * based on the button that was pressed, finding a random Zombie
   * or Human to display in the info panel.
   * @param e The ActionEvent to process.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.infoPanel == null) {
      return;
    }
    String command = e.getActionCommand();
    if (command.equals("selectZombie")) {
      this.infoPanel
          .setSpawnableToShow(this.worldToAccess.selectRandomZombie());
    } else if (command.equals("selectHuman")) {
      this.infoPanel
          .setSpawnableToShow(this.worldToAccess.selectRandomHuman());
    }
  }
  
  
  /** 
   * [setInfoPanel]
   * Sets the SpawnableInfoPanel that the buttons in this
   * panel control.
   * @param infoPanel The SpawnableInfoPanel to control.
   */
  public void setInfoPanel(SpawnableInfoPanel infoPanel) {
    this.infoPanel = infoPanel;
  }
}