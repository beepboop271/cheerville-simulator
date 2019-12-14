import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * [SpawnableInfoPanel]
 * Panel which holds other panels that display
 * information about a Spawnable.
 * 2019-12-06
 * @version 1.2
 * @author Kevin Qiao
 */
public class SpawnableInfoPanel extends JPanel {
  private SpawnableVisionPanel visionPanel;
  private SpawnableTextPanel textPanel;

  
  /** 
   * [spawnableInfoPanel]
   * Constructor for a Spawnable info panel which displays
   * various panels related to a given Spawnable.
   * @param visionPanel The SpawnableVisionPanel which displays what
   *                    a Spawnable can see.
   * @param textPanel   The SpawnableTextPanel which displays information
   *                    about a Spawnable in text.
   * @param buttonPanel The SpawnableButtonPanel which can control
   *                    the Spawnable viewed in this panel.
   */
  public SpawnableInfoPanel(SpawnableVisionPanel visionPanel,
                            SpawnableTextPanel textPanel,
                            SpawnableButtonPanel buttonPanel) {
    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

    this.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 2;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets(5, 5, 5, 5);
    this.add(textPanel, c);
    this.textPanel = textPanel;
    
    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.weightx = 0;
    c.weighty = 0;
    this.add(buttonPanel, c);
    buttonPanel.setInfoPanel(this);
    
    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.5;
    c.weighty = 1.0;
    this.add(visionPanel, c);
    this.visionPanel = visionPanel;
    this.visionPanel.setInfoPanel(this);
    
    this.setOpaque(true);
  }


  /**
   * [update]
   * Ensures this panel does not show a dead Spawnable,
   * switching to a descendant (when possible) if the Spawnable
   * to show has died.
   */
  public void update() {
    if (this.getSpawnableToShow().getHealth() <= 0) {
      this.setSpawnableToShow(this.getSpawnableToShow().getFirstDescendant());
    }
  }

  
  /** 
   * [getSpawnableToShow]
   * Returns the Spawnable this panel is showing information about.
   * @return Spawnable, the Spawnable this panel is showing info about.
   */
  public Spawnable getSpawnableToShow() {
    return this.textPanel.getSpawnableToShow();
  }

  
  /** 
   * [setSpawnableToShow]
   * Sets the Spawnable to display information about in this panel.
   * @param spawnableToShow The Spawnable to show information about.
   */
  public void setSpawnableToShow(Spawnable spawnableToShow) {
    this.textPanel.setSpawnableToShow(spawnableToShow);
    this.visionPanel.setSpawnableToShow(spawnableToShow);   
    this.visionPanel.repaint();
  }
}