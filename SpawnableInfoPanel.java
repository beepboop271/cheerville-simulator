import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class SpawnableInfoPanel extends JPanel {
  private Font infoFont = new Font("Comic Sans MS", Font.PLAIN, 20);
  private Spawnable spawnableToShow;
  private int width;
  private int height;

  public SpawnableInfoPanel(int width, int height) {
    this.width = width;
    this.height = height;
    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    this.setMinimumSize(new Dimension(200, 300));
    this.setPreferredSize(new Dimension(this.width, this.height));
    this.setOpaque(true);
  }

  public void paintComponent(Graphics g) {
    super.repaint();
    setDoubleBuffered(true);

    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(0, 0, this.width, this.height);

    g.setFont(this.infoFont);
    g.setColor(Color.BLACK);

    if(this.spawnableToShow == null) {
      g.drawString("NOTHING SELECTED", 10, 25);
    } else if(this.spawnableToShow.getHealth() <= 0) {
      this.spawnableToShow.removeDeadDescendants();
      this.spawnableToShow.printDescendants();
      this.spawnableToShow = this.spawnableToShow.getFirstDescendant();
    } else {
      int row = 1;
      g.drawString(this.spawnableToShow.toString(), 10, (row++)*25);
      g.drawString("Health:   "+this.spawnableToShow.getHealth(), 10, (row++)*25);
      g.drawString("Position: "+String.format("(%d, %d)",
                                              this.spawnableToShow.getX(),
                                              this.spawnableToShow.getY()),
                   10, (row++)*25);
      if(this.spawnableToShow instanceof Human) {
        g.drawString("Age: "+((Human)this.spawnableToShow).getAge(), 10, (row++)*25);
        g.drawString("Reproduction Chance: "+(int)(((Human)this.spawnableToShow).getBirthChance()*100), 10, (row++)*25);
      }

      String[] descendants = this.spawnableToShow.getDescendantStrings();
      g.setFont(new Font("Courier New", Font.BOLD, 15));
      for(int i = 0; i < descendants.length; ++i) {
        g.drawString(descendants[i], 10, row*25+i*15);
      }

    }
  }

  public void setSpawnableToShow(Spawnable spawnableToShow) {
    this.spawnableToShow = spawnableToShow;
  }

  public Spawnable getSpawnableToShow() {
    return this.spawnableToShow;
  }
}