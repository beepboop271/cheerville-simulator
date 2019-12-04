import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class PercentageGraphPanel extends GraphPanel {
  private double totalSpaces;
  public final Color[] COLOURS = {
    Color.GREEN,
    Color.MAGENTA,
    Color.BLUE,
    Color.RED,
    Color.LIGHT_GRAY
  };

  public PercentageGraphPanel(World worldToDisplay) {
    super(worldToDisplay);
    this.totalSpaces = (double)(worldToDisplay.getHeight()
                                * worldToDisplay.getWidth());
  }

  public double[][] getPercentagesFromCounts(int[][] data) {
    double[][] percentageData = new double[data.length][5];
    double sum;
    for(int i = 0; i < data.length; ++i) {
      sum = 0;
      for(int j = 0; j < 4; ++j) {
        percentageData[i][j] = (data[i][j]/this.totalSpaces)*100;
        sum += percentageData[i][j];
      }
      percentageData[i][4] = 100-sum;
    }
    return percentageData;
  }

  public void paintComponent(Graphics g) {
    // System.out.println("paint % graph");
    super.paintComponent(g);

    this.plotGraph(g,
                    this.getPercentagesFromCounts(this.getDistributionHistory()),
                    this.COLOURS,
                    this.getHeight()/100.0,
                    "%d%%");  // %% produces a literal '%' character
  }
}