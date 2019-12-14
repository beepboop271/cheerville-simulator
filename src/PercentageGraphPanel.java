import java.awt.Color;
import java.awt.Graphics;

/**
 * [PercentageGraphPanel]
 * GraphPanel that displays how much (%) of the map Zombies,
 * Males, Females, and Plants cover over time.
 * 2019-12-13
 * @version 1.2
 * @author Kevin Qiao
 */
public class PercentageGraphPanel extends GraphPanel {
  private double totalSpaces;
  private final Color[] COLORS = {
    Color.GREEN,
    Color.MAGENTA,
    Color.BLUE,
    Color.RED,
    Color.LIGHT_GRAY
  };

  
  /** 
   * [PercentageGraphPanel]
   * Constructor for a graph panel which displays
   * the percentage of spaces in the World filled up
   * by each Spawnable type.
   * @param worldToDisplay The World to graph Spawnable percentages with.
   */
  public PercentageGraphPanel(World worldToDisplay) {
    super(worldToDisplay);
    this.totalSpaces = (double)(worldToDisplay.getHeight()
                                * worldToDisplay.getWidth());
  }

  
  /** 
   * [paintComponent]
   * Plots the data.
   * @param g The Graphics object to draw with.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    this.plotGraph(g,
                    this.getPercentagesFromCounts(this.getDistributionHistory()),
                    this.COLORS,
                    this.getHeight()/100.0,
                    "%d%%");  // %% produces a literal '%' character
  }

  
  /** 
   * [getPercentagesFromCounts]
   * Converts the Spawnable counts stored in the World into
   * percentages to plot on this panel.
   * @param data The Spawnable counts to convert into percentages.
   * @return double[][], the percent composition of Spawnables of
   *         the World over time.
   */
  public double[][] getPercentagesFromCounts(int[][] data) {
    double[][] percentageData = new double[data.length][5];
    double sum;
    for (int i = 0; i < data.length; ++i) {
      sum = 0;
      for (int j = 0; j < 4; ++j) {
        percentageData[i][j] = (data[i][j]/this.totalSpaces)*100;
        sum += percentageData[i][j];
      }
      percentageData[i][4] = 100-sum;
    }
    return percentageData;
  }


  /** 
   * [setTotalSpaces]
   * Sets the total number of cells in the World to calculate
   * percentages properly.
   * @param totalSpaces The total number of cells in the World
   */
  public void setTotalSpaces(double totalSpaces) {
    this.totalSpaces = totalSpaces;
  }
}