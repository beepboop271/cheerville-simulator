import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class WorldPanel extends MapPanel {
  private World worldToDisplay;
  private SpawnableInfoPanel infoPanel;

  private Font smallFont = new Font("Courier New", Font.BOLD, 25);

  public WorldPanel(int width, int height,
                    int cellSize,
                    World worldToDisplay) {
    super(width, height, cellSize, worldToDisplay.getMap());
    this.worldToDisplay = worldToDisplay;
    // this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
  }

  public SpawnableInfoPanel getInfoPanel() {
    return this.infoPanel;
  }

  public void setInfoPanel(SpawnableInfoPanel infoPanel) {
    this.infoPanel = infoPanel;
  }

  public void paintComponent(Graphics g) {
    // System.out.println("paint world");
    super.paintComponent(g);

    // g.setColor(Color.WHITE);
    // g.fillRect(0, 0, this.getWidth(), this.getHeight());
    g.setFont(this.smallFont);

    if(this.getInfoPanel() != null && this.infoPanel.getSpawnableToShow() != null) {
      int x = this.infoPanel.getSpawnableToShow().getX();
      int y = this.infoPanel.getSpawnableToShow().getY();
      g.setColor(Color.BLACK);
      g.fillRect(x*this.getCellSize(), y*this.getCellSize(),
                 this.getCellSize(), this.getCellSize());
      g.setColor(MapPanel.intsToColor(this.infoPanel.getSpawnableToShow().getColor()));
      g.fillRect((x*this.getCellSize())+(int)(this.getCellSize()*0.15),
                 (y*this.getCellSize())+(int)(this.getCellSize()*0.15),
                 (int)(this.getCellSize()*0.7),
                 (int)(this.getCellSize()*0.7));
    }
    // Spawnable s;
    // int channelValue;
    // for (int y = 0; y < this.worldToDisplay.getHeight(); ++y) {
    //   for (int x = 0; x < this.worldToDisplay.getWidth(); ++x) {
    //     if (this.worldToDisplay.isOccupiedAt(x, y)) {
    //       s = this.worldToDisplay.getMapAt(x, y);
    //       channelValue = (s.getMaxHealth()-s.getHealth())*(255/s.getMaxHealth());
    //       if(infoPanel != null && infoPanel.getSpawnableToShow() == s) {
    //         g.setColor(Color.BLACK);
    //         g.fillRect(x*this.cellSize, y*this.cellSize, this.cellSize, this.cellSize); 
    //       }
    //       if (s instanceof Plant) {
    //         g.setColor(new Color(channelValue, 255, channelValue));
    //       } else if (s instanceof Human) {
    //         if (s instanceof Female) {
    //           g.setColor(new Color(255, channelValue, 255));
    //         } else {
    //           g.setColor(new Color(channelValue, channelValue, 255));
    //         }
    //       } else if (s instanceof Zombie) {
    //         g.setColor(new Color(255, channelValue, channelValue));
    //       }
    //       if(infoPanel != null && infoPanel.getSpawnableToShow() == s) {
    //         g.fillRect((x*this.cellSize)+(int)(this.cellSize*0.15),
    //                    (y*this.cellSize)+(int)(this.cellSize*0.15),
    //                    (int)(this.cellSize*0.7),
    //                    (int)(this.cellSize*0.7));
    //       } else {
    //         g.fillRect(x*this.cellSize, y*this.cellSize, this.cellSize, this.cellSize);
    //         g.setColor(Color.BLACK);
    //         // g.drawString(""+s.getID(), x*this.cellSize, y*this.cellSize+(this.cellSize/2));
    //       }
    //     } else {
    //       g.setColor(Color.WHITE);
    //       g.fillRect(x*this.cellSize, y*this.cellSize, this.cellSize, this.cellSize);
    //     }
    //   }
    // }
  }

  @Override
  public void onResize(ComponentEvent e) {
    this.setCellSize((int)(Math.min(getWidth(), getHeight())
                           / this.worldToDisplay.getHeight()));
    this.setSize(new Dimension(this.getCellSize()*this.worldToDisplay.getWidth()+1,
                               this.getCellSize()*this.worldToDisplay.getHeight()+1));
  }

  @Override
  public void onClick(MouseEvent e) {
    int button = e.getButton();
    if(button == MouseEvent.BUTTON1) {
      worldToDisplay.spawnZombieNear(e.getX()/this.getCellSize(),
                                     e.getY()/this.getCellSize());
    } else if(button == MouseEvent.BUTTON3 && infoPanel != null) {
      infoPanel.setSpawnableToShow(worldToDisplay.getMapAt(e.getX()/this.getCellSize(),
                                                           e.getY()/this.getCellSize()));
    }
  }
}