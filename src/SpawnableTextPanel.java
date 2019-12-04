import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SpawnableTextPanel extends JPanel {
  private Spawnable spawnableToShow;
  private Font infoFont = new Font("Comic Sans MS", Font.PLAIN, 20);
  private int width;
  private int height;

  public SpawnableTextPanel(int width, int height) {
    this.width = width;
    this.height = height;
    this.setPreferredSize(new Dimension(width, height));
    this.setOpaque(true);
  }

  public void paintComponent(Graphics g) {
    super.repaint();
    setDoubleBuffered(true);

    g.setColor(new Color(238, 238, 238));
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    g.setFont(this.infoFont);
    g.setColor(Color.BLACK);

    if(this.spawnableToShow == null) {
      this.paintRow(g, "NOTHING", 1);
      this.paintRow(g, "SELECTED", 2);
    // } else if(this.spawnableToShow.getHealth() <= 0) {
    //   this.spawnableToShow.removeDeadDescendants();
    //   this.spawnableToShow.printDescendants();
    //   this.spawnableToShow = this.spawnableToShow.getFirstDescendant();
    } else {
      int row = 1;
      this.paintRow(g, this.spawnableToShow.toString(), row++);
      this.paintRow(g, "Health:   "+this.spawnableToShow.getHealth(), row++);
      this.paintRow(g, "Position: "+String.format("(%d, %d)",
                                                  this.spawnableToShow.getX(),
                                                  this.spawnableToShow.getY()), row++);
      if(this.spawnableToShow instanceof Human) {
        this.paintRow(g, "Age: "+((Human)this.spawnableToShow).getAge(), row++);
        this.paintRow(g, "Reproduction", row++);
        this.paintRow(g, "Chance: "+(int)(((Human)this.spawnableToShow).getBirthChance()*100), row++);
      }

      String[] descendants = this.spawnableToShow.getDescendantStrings();
      g.setFont(new Font("Courier New", Font.BOLD, 15));
      for(int i = 0; i < descendants.length; ++i) {
        g.drawString(descendants[i], 10, row*25+i*15);
      }

    }
  }

  public void paintRow(Graphics g, String rowStr, int row) {
    g.drawString(rowStr, 10, row*25);
  }

  public void setSpawnableToShow(Spawnable spawnableToShow) {
    this.spawnableToShow = spawnableToShow;
  }

  public Spawnable getSpawnableToShow() {
    return this.spawnableToShow;
  }
}