import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SpawnableTextPanel extends JPanel {
  private Spawnable spawnableToShow;
  private Font infoFont = new Font("Courier New", Font.BOLD, 18);
  private Font descendantFont = new Font("Courier New", Font.BOLD, 15);

  public SpawnableTextPanel(int width, int height) {
    this.setPreferredSize(new Dimension(width, height));
    this.setMinimumSize(new Dimension(width, height));
    this.setOpaque(false);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.repaint();
    setDoubleBuffered(true);

    g.setFont(this.infoFont);
    g.setColor(Color.BLACK);

    if (this.spawnableToShow == null) {
      this.paintRow(g, 1, "NOTHING");
      this.paintRow(g, 2, "SELECTED");
    } else {
      int row = 1;
      this.paintRow(g, row++,
        this.spawnableToShow.toString()
      );
      this.paintRow(g, row++,
        String.format("%-10s%d",
                      "Health:",
                      this.spawnableToShow.getHealth())
      );
      this.paintRow(g, row++,
        String.format("%-9s(%d,%d)",
                      "Position:",
                      this.spawnableToShow.getX(), this.spawnableToShow.getY())
      );
      if (this.spawnableToShow instanceof Human) {
        this.paintRow(g, row++,
          String.format("%-10s%d",
                        "Age:",
                        ((Human)this.spawnableToShow).getAge())
        );
        this.paintRow(g, row++, "Reproduction");
        this.paintRow(g, row++,
          String.format("%-10s%d%%",
                        "Chance:",
                        (int)(((Human)this.spawnableToShow).getBirthChance()*100))
        );
      }

      String[] descendants = this.spawnableToShow.getDescendantStrings();
      g.setFont(descendantFont);
      for (int i = 0; i < descendants.length; ++i) {
        g.drawString(descendants[i], 10, row*25+i*15);
      }
    }
  }

  public void paintRow(Graphics g, int row, String rowStr) {
    g.drawString(rowStr, 10, row*25);
  }

  public Spawnable getSpawnableToShow() {
    return this.spawnableToShow;
  }

  public void setSpawnableToShow(Spawnable spawnableToShow) {
    this.spawnableToShow = spawnableToShow;
  }
}