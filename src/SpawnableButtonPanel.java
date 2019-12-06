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
public class SpawnableButtonPanel extends JPanel implements ActionListener{
  private World worldToControl;
  private SpawnableInfoPanel containingPanel;

  public SpawnableButtonPanel(World worldToControl) {
    this.worldToControl = worldToControl;

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

  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.containingPanel == null) {
      return;
    }
    String command = e.getActionCommand();
    if (command.equals("selectZombie")) {
      this.containingPanel
          .setSpawnableToShow(this.worldToControl.selectRandomZombie());
    } else if (command.equals("selectHuman")) {
      System.out.println("hiii");
      this.containingPanel
          .setSpawnableToShow(this.worldToControl.selectRandomHuman());
    }
  }
  
  public void setContainingPanel(SpawnableInfoPanel containingPanel) {
    this.containingPanel = containingPanel;
  }
}