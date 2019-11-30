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
  private int cellSize;
  // private World worldToDisplay;
  private Spawnable[][] mapToDisplay;
  private int mapX, mapY;
  private Rectangle rectToDisplay;

  // private Font smallFont = new Font("Courier New", Font.BOLD, 25);

  public MapPanel(int width, int height, int cellSize, Spawnable[][] mapToDisplay) {
    this.cellSize = cellSize;
    // this.worldToDisplay = worldToDisplay;
    this.mapToDisplay = mapToDisplay;
    addMouseListener(new MapPanelClickListener());
    addComponentListener(new MapPanelResizeListener());
    this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    // this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    this.setPreferredSize(new Dimension(width, height));
    this.setOpaque(true);
  }

  public MapPanel(int width, int height,
                  int cellSize,
                  Spawnable[][] mapToDisplay,
                  Rectangle rectToDisplay) {
    this(width, height, cellSize, mapToDisplay);
    this.rectToDisplay = rectToDisplay;
  }

  public static Color intsToColor(int[] colorArray) {
    if(colorArray.length != 3) {
      throw new IllegalArgumentException();
    }
    return new Color(colorArray[0], colorArray[1], colorArray[2]);
  }

  public void setRectToDisplay(Rectangle rectToDisplay) {
    this.rectToDisplay = rectToDisplay;
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

  public void paintComponent(Graphics g) {
    // System.out.println("paint world");
    super.repaint();
    setDoubleBuffered(true);

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
    // g.setFont(this.smallFont);
    int startX, y;
    int xMax = 0;
    int yMax = 0;
    // System.out.println(this.rectToDisplay);
    if(this.rectToDisplay != null) {
      System.out.println("hi");
      y = this.rectToDisplay.y;
      startX = this.rectToDisplay.x;
      // yMax = y+this.rectToDisplay.height;
      // xMax = x+this.rectToDisplay.width;
    } else {
      y = 0;
      startX = 0;
      yMax = this.mapToDisplay.length;
      xMax = this.mapToDisplay[0].length;
    }
    // System.out.printf("%d %d %d %d\n", x, y, xMax, yMax);
    for (; y < yMax; ++y) {
      for (int x = startX; x < xMax; ++x) {
        // System.out.printf("%d %d\n", x, y);
        if (this.mapToDisplay[y][x] != null) {
          g.setColor(MapPanel.intsToColor(this.mapToDisplay[y][x].getColor()));
          // System.out.println(g.getColor());
          // System.out.printf("%d %d %d %d\n",
              // (this.mapX+x)*this.cellSize, (this.mapY+y)*this.cellSize, this.cellSize, this.cellSize);
          g.fillRect((this.mapX+x)*this.cellSize, (this.mapY+y)*this.cellSize, this.cellSize, this.cellSize);
          // g.setColor(Color.BLACK);
          // g.drawString(""+s.getID(), x*this.cellSize, y*this.cellSize+(this.cellSize/2));
        } else {
          g.setColor(Color.WHITE);
          g.fillRect((this.mapX+x)*this.cellSize, (this.mapY+y)*this.cellSize, this.cellSize, this.cellSize);
        }
      }
    }
  }

  public void onResize(ComponentEvent e) {
    // separate for subclassing
  }

  public void onClick(MouseEvent e) {
    // separate for subclassing
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