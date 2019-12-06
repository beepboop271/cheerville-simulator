import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

@SuppressWarnings("serial")
public class SpawnableVisionPanel extends WorldPanel {
  private Spawnable spawnableToShow;

  
  /** 
   * [SpawnableVisionPanel]
   * Constructor for a panel which displays what
   * a Spawnable can see.
   * @param cellSize        The size to draw each cell of the World visible.
   * @param worldToDisplay  The World that the Spawnable can see.
   * @param spawnableToShow The Spawnable to display info about.
   */
  public SpawnableVisionPanel(int cellSize,
                              World worldToDisplay,
                              Spawnable spawnableToShow) {
    super(cellSize, worldToDisplay);
    this.spawnableToShow = spawnableToShow;
    this.setPreferredSize(new Dimension(cellSize*this.getNumCells(),
                                        cellSize*this.getNumCells()));
    this.setOpaque(true);
  }

  
  /** 
   * [paintComponent]
   * Creates the correct rectangles to draw the Spawnable's vision.
   * If a plant is selected, its surroundings are drawn normally. If
   * a Moveable is selected, the tiles that it can see are drawn
   * in color and other are not.
   * @param g The Graphics object to draw with.
   */
  @Override
  public void paintComponent(Graphics g) {
    // draw gray background for nothing
    if (this.spawnableToShow == null) {
      g.setColor(Color.GRAY);
      g.fillRect(0,
                 0,
                 this.getCellSize()*this.getNumCells(),
                 this.getCellSize()*this.getNumCells());
      return;
    }

    // draw a square around the spawnable of correct size
    int visionSize = this.getWorldToDisplay().getVisionSize();
    Rectangle drawRect = new Rectangle(this.spawnableToShow.getX()-visionSize,
                                       this.spawnableToShow.getY()-visionSize,
                                       this.getNumCells(),
                                       this.getNumCells());
    this.setMapPos(-drawRect.x, -drawRect.y);

    if (this.spawnableToShow instanceof Moveable) {
      // only draw what the spawnable can see in color
      int[] offsets = this.getWorldToDisplay()
                          .getVisionOffsets(((Moveable)this.spawnableToShow)
                                                           .getFacingDirection());
      Rectangle colorRect = new Rectangle(this.spawnableToShow.getX()+offsets[0],
                                           this.spawnableToShow.getY()+offsets[1],
                                           offsets[2], offsets[3]);
      super.paintComponent(g, drawRect, colorRect);

      if ((this.spawnableToShow != null) 
            && (((Moveable)this.spawnableToShow).getInfluences() != null)) {
        // e.g. human converted to zombie and it hasn't moved yet so no influences
        this.paintInfluenceVectors((Graphics2D)g,
                                   ((Moveable)this.spawnableToShow).getInfluences());
      }
    } else {
      super.paintComponent(g, drawRect);
    }
  }

  
  /** 
   * [onResize]
   * Runs when the panel changes size. Recalculates the cell
   * size to fit in the new size and sets the size of this
   * panel to be a square.
   */
  @Override
  public void onResize() {
    this.setCellSize(Math.min(this.getWidth()/this.getNumCells(),
                              this.getHeight()/this.getNumCells()));
    this.setSize(new Dimension(this.getCellSize()*this.getNumCells(),
                               this.getCellSize()*this.getNumCells()));
  }

  
  /** 
   * [onClick]
   * Overrides the onClick method in WorldPanel
   * so that no Zombies are spawned when this panel
   * is clicked.
   * @param e The MouseEvent which triggered this method call.
   */
  @Override
  public void onClick(MouseEvent e) {
    // dont spawn a zombie
  }

  
  /** 
   * [paintInfluenceVectors]
   * Draws the influence vectors that this Moveable considered.
   * @param g2            The Graphics2D object to draw with.
   * @param vectorsToDraw The influence vectors to display.
   */
  public void paintInfluenceVectors(Graphics2D g2, Vector2D[][] vectorsToDraw) {
    // max length will always be the final direction vector, so
    // keep track of that and a second highest length (actual longest influence)
    Vector2D maxLengthVector = new Vector2D(0, 0);
    double secondMaxLength = Double.NEGATIVE_INFINITY;
    for (int i = 0; i < vectorsToDraw.length; ++i) {
      for (int j = 0; j < vectorsToDraw[0].length; ++j) {
        if (vectorsToDraw[i][j] != null) {
          if (vectorsToDraw[i][j].getLength() > maxLengthVector.getLength()) {
            maxLengthVector = vectorsToDraw[i][j];
          } else if (vectorsToDraw[i][j].getLength() > secondMaxLength) {
            secondMaxLength = vectorsToDraw[i][j].getLength();
          }
        }
      }
    }
    // draw lines with antialiasing
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
    Vector2D vectorToDraw;
    double newLength;
    maxLengthVector.setLength(secondMaxLength);
    for (int i = 0; i < vectorsToDraw.length; ++i) {
      for (int j = 0; j < vectorsToDraw[0].length; ++j) {
        if (vectorsToDraw[i][j] != null) {
          // scale all vectors so the longest always takes up the same
          // screen space
          newLength = (vectorsToDraw[i][j].getLength()/secondMaxLength)
                      * (this.getCellSize()*this.getWorldToDisplay().getVisionSize());
          vectorToDraw = (Vector2D)vectorsToDraw[i][j].clone();
          vectorToDraw.setLength(newLength);
          // black outline
          g2.setStroke(new BasicStroke(2.5f));
          g2.setColor(Color.BLACK);
          g2.draw(new Line2D.Double(this.getWidth()/2.0,
                                    this.getHeight()/2.0,
                                    this.getWidth()/2.0+vectorToDraw.getX(),
                                    this.getHeight()/2.0+vectorToDraw.getY()));
          // actual color vector
          g2.setStroke(new BasicStroke(2.0f));
          g2.setColor(vectorToDraw.getColor());
          g2.draw(new Line2D.Double(this.getWidth()/2.0,
                                    this.getHeight()/2.0,
                                    this.getWidth()/2.0+vectorToDraw.getX(),
                                    this.getHeight()/2.0+vectorToDraw.getY()));

        }
      }
    }
  }

  
  /** 
   * [getNumCells]
   * Gets the side length (in cells) of the square to draw
   * in this panel.
   * @return int, the number of cells on each side of this panel.
   */
  public int getNumCells() {
    return (2*this.getWorldToDisplay().getVisionSize())+1;
  }

  
  /** 
   * [getSpawnableToShow]
   * Returns the Spawnable this panel is showing information about.
   * @return Spawnable, the Spawnable this panel is showing info about.
   */
  public Spawnable getSpawnableToShow() {
    return this.spawnableToShow;
  }

  
  /** 
   * [setSpawnableToShow]
   * Sets the Spawnable to display information about in this panel.
   * @param spawnableToShow The Spawnable to show information about.
   */
  public void setSpawnableToShow(Spawnable spawnableToShow) {
    this.spawnableToShow = spawnableToShow;
  }
}