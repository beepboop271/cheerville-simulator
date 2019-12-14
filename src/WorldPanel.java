import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * [WorldPanel]
 * Displays the map in a World, or part of it.
 * 2019-12-13
 * @version 2.1
 * @author Kevin Qiao
 */
public class WorldPanel extends JPanel {
  private World worldToDisplay;
  private SpawnableInfoPanel infoPanel;
  private int cellSize;
  private int mapX, mapY;

  private Font smallFont = new Font("Courier New", Font.BOLD, 25);

  
  /** 
   * [WorldPanel]
   * Constructor for a panel which displays the World.
   * @param cellSize       The size in pixels to draw each cell of the World.
   * @param worldToDisplay The World object to display.
   */
  public WorldPanel(int cellSize, World worldToDisplay) {
    this.cellSize = cellSize;
    this.worldToDisplay = worldToDisplay;
    addMouseListener(new WorldPanelClickListener());
    addComponentListener(new WorldPanelResizeListener());
    this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    this.setPreferredSize(new Dimension(cellSize*this.worldToDisplay.getWidth(),
                                        cellSize*this.worldToDisplay.getHeight()));
    this.setOpaque(true);
  }

  
  /** 
   * [colorToGrayscale]
   * Converts a Color into a grayscale version of that Color.
   * @param clr The Color to convert to grayscale.
   * @return Color, the provided Color as a grayscale Color.
   */
  public static Color colorToGrayscale(Color clr) {
    int minChannel = Math.min(Math.min(clr.getRed(), clr.getGreen()), clr.getBlue());
    return new Color(minChannel, minChannel, minChannel);
  }

  
  /** 
   * [setMapPos]
   * Sets the offset at which to draw the map, in world coordinates.
   * @param x The horizontal position offset to start drawing the map.
   * @param y The vertical position offset to start drawing the map.
   */
  public void setMapPos(int x, int y) {
    this.mapX = x;
    this.mapY = y;
  }

  
  /** 
   * [paintComponent]
   * Paints the panel, drawing the whole map in Color.
   * @param g The Graphics object to draw with.
   */
  @Override
  public void paintComponent(Graphics g) {
    this.paintComponent(g, new Rectangle(0, 0,
                                         this.worldToDisplay.getWidth(),
                                         this.worldToDisplay.getHeight()));
  }

  /** 
   * [paintComponent]
   * Paints the panel, drawing only within the Rectangle (in World
   * coordinates) given (in color).
   * @param g        The Graphics object to draw with.
   * @param drawRect The Rectangle which specifies (in World coordinates)
   *                 what portion of the World to draw.
   */
  public void paintComponent(Graphics g, Rectangle drawRect) {
    this.paintComponent(g, drawRect, drawRect);
  }

  /** 
   * [paintComponent]
   * Paints the panel, drawing only within the Rectangle drawRect, and
   * only drawing in color within the Rectangle colorRect (both in World
   * coordinates).
   * @param g         The Graphics object to draw with.
   * @param drawRect  The Rectangle which specifies (in World coordinates)
   *                  what portion of the World to draw.
   * @param colorRect The Rectangle which specifies (in World coordinates)
   *                  what portion of the World to draw in color.
   */
  public void paintComponent(Graphics g, Rectangle drawRect, Rectangle colorRect) {
    super.repaint();
    setDoubleBuffered(true);

    g.setColor(this.getBackground());
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    for (int y = drawRect.y; y < drawRect.y+drawRect.height; ++y) {
      for (int x = drawRect.x; x < drawRect.x+drawRect.width; ++x) {
        if (this.worldToDisplay.isInWorld(x, y)) {
          if (this.worldToDisplay.getMapAt(x, y) != null) {
            if (colorRect.contains(x, y)) {
              g.setColor(this.worldToDisplay.getMapAt(x, y).getColor());
            } else {
              g.setColor(WorldPanel.colorToGrayscale(this.worldToDisplay
                                                         .getMapAt(x, y)
                                                         .getColor()));
            }
            g.fillRect((this.mapX+x)*this.cellSize,
                       (this.mapY+y)*this.cellSize,
                       this.cellSize, this.cellSize);
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

    if ((this.infoPanel != null) && (this.infoPanel.getSpawnableToShow() != null)) {
      this.paintSelectedCell(g,
                             this.infoPanel.getSpawnableToShow().getX(),
                             this.infoPanel.getSpawnableToShow().getY());
      this.infoPanel.update();
    }
  }

  
  /** 
   * [paintSelectedCell]
   * Draws a black border around a cell, emphasizing it. Used to
   * draw which cell is selected.
   * @param g The Graphics object to draw with.
   * @param x The x coordinate of the cell selected.
   * @param y The y coordinate of the cell selected.
   */
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

  
  /** 
   * [onResize]
   * Updates the cellSize whenever the panel is resized so that
   * the map takes up as much space as it can.
   */
  public void onResize() {
    this.setCellSize(Math.min(this.getWidth()/this.worldToDisplay.getWidth(),
                              this.getHeight()/this.worldToDisplay.getHeight()));
  }

  
  /** 
   * [onClick]
   * Handles a mouse click event. Spawns a Zombie for left-click, Human
   * for middle-click, and selects a cell for right-click.
   * @param e The MouseEvent that was emitted.
   */
  public void onClick(MouseEvent e) {
    int button = e.getButton();
    if (button == MouseEvent.BUTTON1) {
      this.worldToDisplay.spawnZombieNear(e.getX()/this.getCellSize(),
                                          e.getY()/this.getCellSize());
    } else if (button == MouseEvent.BUTTON2) {
      this.worldToDisplay.spawnHumanNear(e.getX()/this.getCellSize(),
                                         e.getY()/this.getCellSize());
    } else if ((button == MouseEvent.BUTTON3) && (this.infoPanel != null)) {
      this.infoPanel.setSpawnableToShow(this.worldToDisplay
                                            .getMapAt(e.getX()/this.getCellSize(),
                                                      e.getY()/this.getCellSize()));
    }
  }

  
  /** 
   * [getCellSize]
   * Returns the size, in pixels, of each cell of the map.
   * @return int, the number of pixels per each cell of the map.
   */
  public int getCellSize() {
    return this.cellSize;
  }

  
  /** 
   * [setCellSize]
   * Sets the size, in pixels, of each cell of the map.
   * @param cellSize The number of pixels per each cell of the map.
   */
  public void setCellSize(int cellSize) {
    this.cellSize = cellSize;
  }

  
  /** 
   * [getInfoPanel]
   * Returns the SpawnableInfoPanel which this World is
   * associated to and controls.
   * @return SpawnableInfoPanel, the info panel this World is
   *         associated to and controls.
   */
  public SpawnableInfoPanel getInfoPanel() {
    return this.infoPanel;
  }

  
  /** 
   * [setInfoPanel]
   * Sets the SpawnableInfoPanel which this World is
   * associated to and controls.
   * @param infoPanel The SpawnableInfopanel to associate to
   *                  this WorldPanel.
   */
  public void setInfoPanel(SpawnableInfoPanel infoPanel) {
    this.infoPanel = infoPanel;
  }


  // Inner classes:

  /**
   * [WorldPanelResizeListener]
   * Calls a method whenever the panel is resized.
   * @version 1.0
   * @author Kevin Qiao
   */
  public class WorldPanelResizeListener extends ComponentAdapter {
    public void componentResized(ComponentEvent e) {
      onResize();
    }
  }

  /**
   * [WorldPanelClickListener]
   * Calls a mmethod whenever the mouse is clicked.
   * @version 1.0
   * @author Kevin Qiao
   */
  public class WorldPanelClickListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      onClick(e);
    }
  }
}