import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MapPanel extends JPanel {
  private int width;
  private int height;
  private int cellSize;
  private World worldToDisplay;
  private Rectangle rectToDisplay;

  // private Font smallFont = new Font("Courier New", Font.BOLD, 25);

  public MapPanel(int width, int height, int cellSize, World worldToDisplay) {
    this.width = width;
    this.height = height;
    this.cellSize = cellSize;
    this.worldToDisplay = worldToDisplay;
    addMouseListener(new WorldPanelClickListener());
    addComponentListener(new WorldPanelResizeListener());
    this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    // this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    this.setPreferredSize(new Dimension(this.width, this.height));
    this.setOpaque(true);
  }

  public MapPanel(int width, int height,
                  int cellSize,
                  World worldToDisplay,
                  Rectangle rectToDisplay) {
    this(width, height, cellSize, worldToDisplay);
    this.rectToDisplay = rectToDisplay;
  }

  public void paintComponent(Graphics g) {
    // System.out.println("paint world");
    super.repaint();
    setDoubleBuffered(true);

    // g.setColor(Color.WHITE);
    // g.fillRect(0, 0, this.getWidth(), this.getHeight());
    // g.setFont(this.smallFont);

    Spawnable s;
    int channelValue;
    for (int y = 0; y < this.worldToDisplay.getHeight(); ++y) {
      for (int x = 0; x < this.worldToDisplay.getWidth(); ++x) {
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
          // g.setColor(Color.BLACK);
          // g.drawString(""+s.getID(), x*this.cellSize, y*this.cellSize+(this.cellSize/2));
        } else {
          g.setColor(Color.WHITE);
          g.fillRect(x*this.cellSize, y*this.cellSize, this.cellSize, this.cellSize);
        }
      }
    }
  }

  public class WorldPanelResizeListener extends ComponentAdapter {
    public void componentResized(ComponentEvent e) {
      cellSize = (int)(Math.min(getWidth(), getHeight())/worldToDisplay.getHeight());
      setSize(new Dimension(cellSize*worldToDisplay.getWidth()+1,
              cellSize*worldToDisplay.getHeight()+1));
    }
  }

  public class WorldPanelClickListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      int button = e.getButton();
      if(button == MouseEvent.BUTTON1) {
        worldToDisplay.spawnZombieNear(e.getX()/cellSize, e.getY()/cellSize);
      // } else if(button == MouseEvent.BUTTON3 && infoPanel != null) {
      //   infoPanel.setSpawnableToShow(worldToDisplay.getMapAt(e.getX()/cellSize,
      //                                                        e.getY()/cellSize));
      }
    }
  }
}