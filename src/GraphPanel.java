import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class GraphPanel extends JPanel {
  private Font graphFont = new Font("Courier New", Font.BOLD, 20);
  private int graphWidth;
  private World worldToDisplay;

  public GraphPanel(World worldToDisplay) {
    super();
    this.worldToDisplay = worldToDisplay;
    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    this.graphWidth = this.worldToDisplay.getHistoryAmount();
    addComponentListener(new GraphPanelResizeListener());
    
    this.setPreferredSize(new Dimension(this.graphWidth+60, 200));

    this.setOpaque(true);
  }

  public static int clamp(int val, int min, int max) {
    return Math.min(max, Math.max(min, val));
  }

  public static int sum(int[] record) {
    int total = 0;
    for(int i = 0; i < record.length; ++i) {
      total += record[i];
    }
    return total;
  }

  public static int findMax(int[][] history, int startIdx) {
    int count;
    int maxValue = -1;
    for(int i = startIdx; i < history.length; ++i) {
      count = GraphPanel.sum(history[i]);
      if(count > maxValue) {
        maxValue = count;
      }
    }
    return maxValue;
  }

  public static int getTextRow(int graphRow, int lastTextRow, int height) {
    int textRow = GraphPanel.clamp(graphRow+8, 20, height-5);
    if(lastTextRow != -1 && (textRow-lastTextRow < 20)) {
      textRow = GraphPanel.clamp(lastTextRow+20, 20, height-5);
    }
    return textRow;
  }

  public int getGraphWidth() {
    return this.graphWidth;
  }

  public int[][] getDistributionHistory() {
    return this.worldToDisplay.getDistributionHistory();
  }

  public void paintComponent(Graphics g) {
    super.repaint();
    setDoubleBuffered(true);

    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    g.setFont(this.graphFont);
  }

  public void plotGraph(Graphics g,
                        double[][] data,
                        Color[] colours,
                        double scaleFactor,
                        String fstring) {
    double[] record;
    int row;
    int lastTextRow = -1;
    for(int col = 0; col < data.length; ++col) {
      record = data[col];
      row = 0;
      for(int i = 0; i < record.length; ++i) {
        g.setColor(colours[i]);
        if(col == data.length-1) {
          lastTextRow = GraphPanel.getTextRow(row, lastTextRow,
                                              this.getHeight());
          g.drawString(String.format(fstring, (int)(record[i])),
                       col+10, lastTextRow);
        }
        
        if(record[i] > 0) {
          g.drawLine(col, row, col, row+((int)(record[i]*scaleFactor)));
          row += (int)(record[i]*scaleFactor);
        }
      }
    }
  }

  public void plotGraph(Graphics g,
                        int[][] data,
                        Color[] colours,
                        double scaleFactor,
                        String fstring) {
    double[][] convertedData = new double[data.length][data[0].length];
    for(int i = 0; i < data.length; ++i) {
      for(int j = 0; j < data[0].length; ++j) {
        convertedData[i][j] = (double)(data[i][j]);
      }
    }
    this.plotGraph(g, convertedData, colours, scaleFactor, fstring);
  }

  public void resize() {
    this.graphWidth = this.getWidth()-60;
    this.worldToDisplay.setHistoryAmount(graphWidth);
  }

  public class GraphPanelResizeListener extends ComponentAdapter {
    public void componentResized(ComponentEvent e) {
      // call method in GraphPanel so that
      // CountGraphPanel can override it
      resize();
    }
  }
}