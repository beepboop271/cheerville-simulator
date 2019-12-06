import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class CountGraphPanel extends GraphPanel {
  private int maxMoveableCount = -1;
  public final Color[] COLOURS = {
    Color.MAGENTA,
    Color.BLUE,
    Color.RED
  };

  public CountGraphPanel(World worldToDisplay) {
    super(worldToDisplay);
  }

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
                   this.COLOURS,
                   ((double)this.getHeight())/this.maxMoveableCount,
                   "%d");
  }

  @Override
  public void onResize() {
    super.onResize();
    this.maxMoveableCount = GraphPanel.findMax(
        this.extractMoveableCounts(this.getDistributionHistory()), 0);
  }

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