import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class WorldPanel extends JPanel {
  private World worldToDisplay;
  private SpawnableInfoPanel infoPanel;
  private int cellSize;
  private int mapX, mapY;

  private Font smallFont = new Font("Courier New", Font.BOLD, 25);

  public WorldPanel(int cellSize, World worldToDisplay) {
    this.cellSize = cellSize;
    this.worldToDisplay = worldToDisplay;
    addMouseListener(new MapPanelClickListener());
    addComponentListener(new MapPanelResizeListener());
    this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    this.setPreferredSize(new Dimension(cellSize*this.worldToDisplay.getWidth(),
                                        cellSize*this.worldToDisplay.getHeight()));
    this.setOpaque(true);
  }

  public static Color colorToGrayscale(Color clr) {
    int minChannel = Math.min(Math.min(clr.getRed(), clr.getGreen()), clr.getBlue());
    return new Color(minChannel, minChannel, minChannel);
  }

  public void setMapPos(int x, int y) {
    this.mapX = x;
    this.mapY = y;
  }

  public int getCellSize() {
    return this.cellSize;
  }

  public void setCellSize(int cellSize) {
    this.cellSize = cellSize;
  }

  public SpawnableInfoPanel getInfoPanel() {
    return this.infoPanel;
  }

  public void setInfoPanel(SpawnableInfoPanel infoPanel) {
    this.infoPanel = infoPanel;
  }

  public World getWorldToDisplay() {
    return this.worldToDisplay;
  }

  public void paintComponent(Graphics g) {
    this.paintComponent(g, new Rectangle(0, 0,
                                         this.worldToDisplay.getWidth(),
                                         this.worldToDisplay.getHeight()));
  }

  public void paintComponent(Graphics g, Rectangle drawRect) {
    this.paintComponent(g, drawRect, drawRect);
  }

  public void paintComponent(Graphics g, Rectangle drawRect, Rectangle colourRect) {
    super.repaint();
    setDoubleBuffered(true);

    g.setColor(Color.GRAY);
    g.fillRect(0, 0, this.getCellSize()*drawRect.width, this.getCellSize()*drawRect.height);

    for (int y = drawRect.y; y < drawRect.y+drawRect.height; ++y) {
      for (int x = drawRect.x; x < drawRect.x+drawRect.width; ++x) {
        if (this.worldToDisplay.isInWorld(x, y)) {
          if (this.worldToDisplay.getMapAt(x, y) != null) {
            if (colourRect.contains(x, y)) {
              g.setColor(this.worldToDisplay.getMapAt(x, y).getColor());
            } else {
              g.setColor(WorldPanel.colorToGrayscale(this.worldToDisplay
                                                         .getMapAt(x, y)
                                                         .getColor()));
            }
            g.fillRect((this.mapX+x)*this.cellSize,
                       (this.mapY+y)*this.cellSize,
                       this.cellSize, this.cellSize);
            // g.setColor(Color.BLACK);
            // g.drawString(""+s.getID(), x*this.cellSize, y*this.cellSize+(this.cellSize/2));
          } else {
            g.setColor(Color.WHITE);
            g.fillRect((this.mapX+x)*this.cellSize,
                       (this.mapY+y)*this.cellSize,
                       this.cellSize, this.cellSize);
          }
        }
      }
    }

    g.setFont(this.smallFont);

    if(this.infoPanel != null && this.infoPanel.getSpawnableToShow() != null) {
      this.paintSelectedCell(g,
                             this.infoPanel.getSpawnableToShow().getX(),
                             this.infoPanel.getSpawnableToShow().getY());
      this.infoPanel.update();
    }
  }

  public void paintSelectedCell(Graphics g, int x, int y) {
    g.setColor(Color.BLACK);
    g.fillRect((this.mapX+x)*this.getCellSize(), (this.mapY+y)*this.getCellSize(),
               this.getCellSize(), this.getCellSize());

    g.setColor(this.infoPanel.getSpawnableToShow().getColor());
    g.fillRect(((this.mapX+x)*this.getCellSize())+(int)(this.getCellSize()*0.15),
               ((this.mapY+y)*this.getCellSize())+(int)(this.getCellSize()*0.15),
               (int)(this.getCellSize()*0.7),
               (int)(this.getCellSize()*0.7));
  }

  public void onResize(ComponentEvent e) {
    this.setCellSize(Math.min(this.getWidth()/this.worldToDisplay.getWidth(),
                              this.getHeight()/this.worldToDisplay.getHeight()));
    this.setSize(new Dimension(this.getCellSize()*this.worldToDisplay.getWidth(),
                               this.getCellSize()*this.worldToDisplay.getHeight()));
  }

  public void onClick(MouseEvent e) {
    int button = e.getButton();
    if(button == MouseEvent.BUTTON1) {
      this.worldToDisplay.spawnZombieNear(e.getX()/this.getCellSize(),
                                          e.getY()/this.getCellSize());
    } else if(button == MouseEvent.BUTTON2) {
      this.worldToDisplay.spawnHumanNear(e.getX()/this.getCellSize(),
                                         e.getY()/this.getCellSize());
    } else if(button == MouseEvent.BUTTON3 && this.infoPanel != null) {
      this.infoPanel.setSpawnableToShow(this.worldToDisplay
                                            .getMapAt(e.getX()/this.getCellSize(),
                                                      e.getY()/this.getCellSize()));
    }
  }

  public class MapPanelResizeListener extends ComponentAdapter {
    public void componentResized(ComponentEvent e) {
      onResize(e);
    }
  }

  public class MapPanelClickListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      onClick(e);
    }
  }
}