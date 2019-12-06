import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SpawnableInfoPanel extends JPanel {
  private SpawnableVisionPanel visionPanel;
  private SpawnableTextPanel textPanel;

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
    buttonPanel.setContainingPanel(this);
    
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

  public void update() {
    if (this.getSpawnableToShow().getHealth() <= 0) {
      this.setSpawnableToShow(this.getSpawnableToShow().getFirstDescendant());
    }
  }

  public Spawnable getSpawnableToShow() {
    return this.textPanel.getSpawnableToShow();
  }

  public void setSpawnableToShow(Spawnable spawnableToShow) {
    this.textPanel.setSpawnableToShow(spawnableToShow);
    this.visionPanel.setSpawnableToShow(spawnableToShow);   
  }
}