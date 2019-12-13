import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public abstract class GraphPanel extends JPanel {
  private Font graphFont = new Font("Courier New", Font.BOLD, 20);
  private int graphWidth;
  private World worldToDisplay;

  
  /** 
   * [GraphPanel]
   * Constructor for a graph panel which plots data about
   * a World.
   * @param worldToDisplay The World which contains useful data.
   */
  public GraphPanel(World worldToDisplay) {
    super();
    this.worldToDisplay = worldToDisplay;
    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    this.graphWidth = this.worldToDisplay.getHistoryAmount();
    addComponentListener(new GraphPanelResizeListener());
    
    this.setPreferredSize(new Dimension(this.graphWidth+60, 200));

    this.setOpaque(true);
  }

  
  /** 
   * [clamp]
   * Clamps a value within the interval [min, max].
   * @param val The value to clamp.
   * @param min The inclusive minimum value to produce.
   * @param max The inclusive maximum value to produce.
   * @return int, the value clamped inside the specified interval.
   */
  public static int clamp(int val, int min, int max) {
    return Math.min(max, Math.max(min, val));
  }

  
  /** 
   * [sum]
   * Finds the sum of all values in an int[].
   * @param record The int[] to sum.
   * @return int, the total sum of all values in the int[].
   */
  public static int sum(int[] record) {
    int total = 0;
    for (int i = 0; i < record.length; ++i) {
      total += record[i];
    }
    return total;
  }

  
  /** 
   * [findMax]
   * Given a 2D array which stores values to plot over time,
   * find the record in time with the highest sum of values.
   * Used for calculating the vertical scaling required on a
   * graph, since the graph needs to fit the maximum sum of
   * values.
   * @param history  The int[][] which stores values to plot over time.
   * @param startIdx The index to start searching for a maximum value from.
   * @return int, the maximum sum of values at a stored point in time.
   */
  public static int findMax(int[][] history, int startIdx) {
    int count;
    int maxValue = -1;
    for (int i = startIdx; i < history.length; ++i) {
      count = GraphPanel.sum(history[i]);
      if (count > maxValue) {
        maxValue = count;
      }
    }
    return maxValue;
  }

  
  /** 
   * [getTextRow]
   * Calculates the y coordinate to draw a text label at, taking into
   * account the space in the panel and other text labels. The new
   * label must not overlap the previous label, and it should fit in
   * the panel.
   * @param graphRow    The ideal y coordinate to place the label at,
   *                    next to the top of the graph plot being labelled.
   * @param lastTextRow The y coordinate of the last text label to take
   *                    into account, preventing overlapping labels.
   * @param height      The height of the panel.
   * @return int, the correct y coordinate to draw the text at.
   */
  public static int getTextRow(int graphRow, int lastTextRow, int height) {
    // fit the text in the panel
    int textRow = GraphPanel.clamp(graphRow+8, 20, height-5);
    if ((lastTextRow != -1) && (textRow-lastTextRow < 20)) {
      // if the last label is too close, shift this label a bit
      // but keep it in the panel
      textRow = GraphPanel.clamp(lastTextRow+20, 20, height-5);
    }
    return textRow;
  }

  
  /** 
   * [paintComponent]
   * Calls JFrame painting methods and fills in the background.
   * @param g The Graphics object to draw with.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.repaint();
    setDoubleBuffered(true);

    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
  }

  
  /** 
   * [plotGraph]
   * Draws the double[][] data onto the panel with the
   * given colors, vertical scale, and label format string.
   * @param g           The Graphics object to draw with.
   * @param data        The double values over time to plot.
   * @param colors      The colours to draw each section in.
   * @param scaleFactor The amount to vertically scale the data.
   * @param fstring     The format string to draw labels with, must
   *                    contain one %d for the value of the data.
   */
  public void plotGraph(Graphics g,
                        double[][] data,
                        Color[] colors,
                        double scaleFactor,
                        String fstring) {
    double[] record;
    int row;
    int lastTextRow = -1;
    g.setFont(this.graphFont);
    for (int col = 0; col < data.length; ++col) {
      record = data[col];
      row = 0;
      for (int i = 0; i < record.length; ++i) {
        g.setColor(colors[i]);
        if (col == data.length-1) {
          lastTextRow = GraphPanel.getTextRow(row, lastTextRow,
                                              this.getHeight());
          g.drawString(String.format(fstring, (int)(record[i])),
                       col+10, lastTextRow);
        }
        
        if (record[i] > 0) {
          g.drawLine(col, row, col, row+((int)(record[i]*scaleFactor)));
          row += (int)(record[i]*scaleFactor);
        }
      }
    }
  }

  /** 
   * [plotGraph]
   * Draws the int[][] data onto the panel with the
   * given colors, vertical scale, and label format string.
   * @param g           The Graphics object to draw with.
   * @param data        The integer values over time to plot.
   * @param colors      The colours to draw each section in.
   * @param scaleFactor The amount to vertically scale the data.
   * @param fstring     The format string to draw labels with, must
   *                    contain one %d for the value of the data.
   */
  public void plotGraph(Graphics g,
                        int[][] data,
                        Color[] colors,
                        double scaleFactor,
                        String fstring) {
    double[][] convertedData = new double[data.length][data[0].length];
    for (int i = 0; i < data.length; ++i) {
      for (int j = 0; j < data[0].length; ++j) {
        convertedData[i][j] = (double)(data[i][j]);
      }
    }
    this.plotGraph(g, convertedData, colors, scaleFactor, fstring);
  }


  /**
   * [onResize]
   * Runs when the panel changes size. Updates how much history
   * the World should record to plot on the graph.
   */
  public void onResize() {
    this.graphWidth = this.getWidth()-60;
    this.worldToDisplay.setHistoryAmount(graphWidth);
  }

  
  /** 
   * [getDistributionHistory]
   * Gets the data which stores counts of various Spawnables
   * over time.
   * @return int[][], the Spawnable counts over time.
   */
  public int[][] getDistributionHistory() {
    return this.worldToDisplay.getDistributionHistory();
  }


  /**
   * [getWorldToDisplay]
   * Gets the World this panel is displaying information about.
   * @return World, the World this panel is displaying.
   */
  public World getWorldToDisplay() {
    return this.worldToDisplay;
  }


  public class GraphPanelResizeListener extends ComponentAdapter {
    public void componentResized(ComponentEvent e) {
      // call method in GraphPanel so that
      // CountGraphPanel can override it
      onResize();
    }
  }
}