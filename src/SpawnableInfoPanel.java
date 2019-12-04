import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
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
                            SpawnableTextPanel textPanel) {
    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

    this.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 2;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.6;
    c.weighty = 1.0;
    c.insets = new Insets(10, 10, 10, 10);
    this.add(textPanel, c);
    this.textPanel = textPanel;
    
    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    // c.fill = GridBagConstraints.BOTH;
    // c.weightx = 0.4;
    // c.weighty = 1.0;
    c.insets = new Insets(10, 0, 10, 10);
    this.add(visionPanel, c);
    this.visionPanel = visionPanel;
    this.visionPanel.setInfoPanel(this);

    this.setOpaque(true);
  }

  public void update() {
    if(this.getSpawnableToShow().getHealth() <= 0) {
      System.out.println("aaaadsffds");
      this.setSpawnableToShow(this.getSpawnableToShow().getFirstDescendant());
      System.out.println(this.getSpawnableToShow());
      // System.out.println(this.getSpawnableToShow().getHealth());
    }
  }

  public void setSpawnableToShow(Spawnable spawnableToShow) {
    this.textPanel.setSpawnableToShow(spawnableToShow);
    this.visionPanel.setSpawnableToShow(spawnableToShow);   
  }

  public Spawnable getSpawnableToShow() {
    return this.textPanel.getSpawnableToShow();
  }
}