import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

@SuppressWarnings("serial")
public class SpawnableVisionPanel extends WorldPanel {
  private Spawnable spawnableToShow;

  public SpawnableVisionPanel(int cellSize,
                              World worldToDisplay,
                              Spawnable spawnableToShow) {
    super(cellSize, worldToDisplay);
    this.spawnableToShow = spawnableToShow;
    this.setPreferredSize(new Dimension(cellSize*((worldToDisplay.getVisionSize()*2)+1),
                                        cellSize*((worldToDisplay.getVisionSize()*2)+1)));
  }

  public void paintComponent(Graphics g) {
    if(this.spawnableToShow == null) {
      g.setColor(Color.GRAY);
      g.fillRect(0, 0, this.getWidth(), this.getHeight());
      return;
    }
    int visionSize = this.getWorldToDisplay().getVisionSize();
    Rectangle drawRect = new Rectangle(this.spawnableToShow.getX()-visionSize,
                                       this.spawnableToShow.getY()-visionSize,
                                       (2*visionSize)+1, (2*visionSize)+1);
    this.setMapPos(-drawRect.x, -drawRect.y);
    if(this.spawnableToShow instanceof Moveable) {
      int[] offsets = this.getWorldToDisplay()
                          .getVisionOffsets(((Moveable)this.spawnableToShow)
                                                           .getFacingDirection());
      Rectangle colourRect = new Rectangle(this.spawnableToShow.getX()+offsets[0],
                                           this.spawnableToShow.getY()+offsets[1],
                                           offsets[2], offsets[3]);
      super.paintComponent(g, drawRect, colourRect);
      if(this.spawnableToShow != null) {
        this.paintInfluenceVectors((Graphics2D)g,
                                   ((Moveable)this.spawnableToShow).getInfluences());
      }
    } else {
      super.paintComponent(g, drawRect);
    }
  }

  public void paintInfluenceVectors(Graphics2D g2, Vector2D[][] vectorsToDraw) {
    double maxLength = Double.NEGATIVE_INFINITY;
    for(int i = 0; i < vectorsToDraw.length; ++i) {
      for(int j = 0; j < vectorsToDraw[0].length; ++j) {
        if ((vectorsToDraw[i][j] != null)
              && (vectorsToDraw[i][j].getLength() > maxLength)) {
          maxLength = vectorsToDraw[i][j].getLength();
        }
      }
    }
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
    Vector2D vectorToDraw;
    double newLength;
    for(int i = 0; i < vectorsToDraw.length; ++i) {
      for(int j = 0; j < vectorsToDraw[0].length; ++j) {
        if(vectorsToDraw[i][j] != null) {
          newLength = (vectorsToDraw[i][j].getLength()/maxLength)
                      * (this.getCellSize()*this.getWorldToDisplay().getVisionSize());
          vectorToDraw = (Vector2D)vectorsToDraw[i][j].clone();
          vectorToDraw.setLength(newLength);
          g2.setStroke(new BasicStroke(2.5f));
          // g2.setStroke(new BasicStroke(Math.max((float)(5.0
          //                                               * vectorsToDraw[i][j].getLength()
          //                                               / maxLength),
          //                                       1.0f)));
          g2.setColor(Color.BLACK);
          g2.draw(new Line2D.Double(this.getWidth()/2.0,
                                    this.getHeight()/2.0,
                                    this.getWidth()/2.0+vectorToDraw.getX(),
                                    this.getHeight()/2.0+vectorToDraw.getY()));
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

  public void setSpawnableToShow(Spawnable spawnableToShow) {
    this.spawnableToShow = spawnableToShow;
  }

  public Spawnable getSpawnableToShow() {
    return this.spawnableToShow;
  }

  @Override
  public void onResize(ComponentEvent e) {
    // do nothing
  }

  @Override
  public void onClick(MouseEvent e) {
    // dont spawn a zombie
  }
}