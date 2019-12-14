import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class CountGraphPanel extends GraphPanel {
  private int maxMoveableCount = -1;
  private final Color[] COLORS = {
    Color.MAGENTA,
    Color.BLUE,
    Color.RED
  };

  
  /** 
   * [CountGraphPanel]
   * Constructor for a graph panel which displays the pure
   * counts of Spawnables in a given world.
   * @param worldToDisplay The World to graph Spawnable counts with.
   */
  public CountGraphPanel(World worldToDisplay) {
    super(worldToDisplay);
  }

  
  /** 
   * [paintComponent]
   * Determines how much to vertically scale the graph and
   * plots the data.
   * @param g The Graphics object to draw with.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    int[][] history = this.extractMoveableCounts(this.getDistributionHistory());

    if (this.maxMoveableCount == -1) {
      this.maxMoveableCount = GraphPanel.findMax(history, 0);
    } else if (GraphPanel.sum(history[0]) == this.maxMoveableCount) {
      // if the max is in the first record, it means it is the
      // oldest and will be removed immediately after, so
      // recalculate the max
      this.maxMoveableCount = GraphPanel.findMax(history, 1);
    } else if (GraphPanel.sum(history[history.length-1]) > this.maxMoveableCount) {
      this.maxMoveableCount = GraphPanel.sum(history[history.length-1]);
    }

    this.plotGraph(g,
                   history,
                   this.COLORS,
                   ((double)this.getHeight())/this.maxMoveableCount,
                   "%d");
  }


  /**
   * [onResize]
   * Runs when the panel changes size. Recalculates how much
   * to vertically scale the graph since the data being displayed
   * will change.
   */
  @Override
  public void onResize() {
    super.onResize();
    this.maxMoveableCount = GraphPanel.findMax(
        this.extractMoveableCounts(this.getDistributionHistory()), 0);
  }

  
  /** 
   * [extractMoveableCounts]
   * Converts an array which stores the counts of all Spawnables
   * over time into an array which stores the counts of Moveables
   * over time.
   * @param data The 2D array of Spawnable counts over time to convert.
   * @return int[][], the 2D array of Moveable counts over time.
   */
  public int[][] extractMoveableCounts(int[][] data) {
    int[][] moveableData = new int[data.length][3];
    for (int i = 0; i < data.length; ++i) {
      moveableData[i][0] = data[i][1];
      moveableData[i][1] = data[i][2];
      moveableData[i][2] = data[i][3];
    }
    return moveableData;
  }
}