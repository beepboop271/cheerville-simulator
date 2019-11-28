import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;

import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class WorldPanel extends JPanel {
  private int width;
  private int height;
  private int cellSize;
  private World worldToDisplay;

  public WorldPanel(int width, int height, int cellSize, World worldToDisplay) {
    this.width = width;
    this.height = height;
    this.cellSize = cellSize;
    this.worldToDisplay = worldToDisplay;

    addMouseListener(new WorldPanelMouseListener());
    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    this.setMinimumSize(new Dimension(this.width, this.height));
    this.setPreferredSize(new Dimension(this.width, this.height));
    this.setOpaque(true);
  }

  public void paintComponent(Graphics g) {
    // System.out.println("paint world");
    super.repaint();
    setDoubleBuffered(true);

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, this.width, this.height);

    Spawnable s;
    int channelValue;
    for (int y = 0; y < this.worldToDisplay.HEIGHT; ++y) {
      for (int x = 0; x < this.worldToDisplay.WIDTH; ++x) {
        if (this.worldToDisplay.isOccupiedAt(x, y)) {
          s = this.worldToDisplay.getMapAt(x, y);
          channelValue = (s.getMaxHealth()-s.getHealth())*(255/s.getMaxHealth());
          if (s instanceof Plant) {
            g.setColor(new Color(channelValue, 255, channelValue));
          } else if (s instanceof Human) {
            if (s instanceof Female) {
              g.setColor(new Color(255, channelValue, 255));
            } else {
              g.setColor(new Color(channelValue, channelValue, 255));
            }
          } else if (s instanceof Zombie) {
            g.setColor(new Color(255, channelValue, channelValue));
          }
          g.fillRect(x*this.cellSize, y*this.cellSize, this.cellSize, this.cellSize);
        }
      }
    }
  }

  public class WorldPanelMouseListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      worldToDisplay.spawnZombieNear(e.getX()/cellSize, e.getY()/cellSize);
    }
  }
}