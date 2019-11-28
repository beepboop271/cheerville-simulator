import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CheervilleFrame extends JFrame {
  public CheervilleFrame(String title, World worldToDisplay) {
    super(title);

    // actually not that bad tutorial:
    // https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
    JPanel mainPane = new JPanel();
    mainPane.setLayout(new GridBagLayout());

    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 3;
    c.insets = new Insets(10, 10, 10, 10);
    int cellSize = (int)((Toolkit.getDefaultToolkit()
                                 .getScreenSize()
                                 .height*0.7)
                         / worldToDisplay.HEIGHT);
    mainPane.add(new WorldPanel(cellSize*worldToDisplay.WIDTH + 1,
                                cellSize*worldToDisplay.HEIGHT + 1,
                                cellSize,
                                worldToDisplay),
                 c);

    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.insets = new Insets(10, 0, 10, 10);
    mainPane.add(new PercentageGraphPanel(worldToDisplay.HISTORY_AMOUNT,
                                          worldToDisplay,
                                          worldToDisplay.WIDTH*worldToDisplay.HEIGHT),
                 c);

    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.insets = new Insets(0, 0, 10, 10);
    mainPane.add(new CountGraphPanel(worldToDisplay.HISTORY_AMOUNT,
                                     worldToDisplay),
                 c);

    mainPane.setOpaque(true);
    this.setContentPane(mainPane);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
    this.setVisible(true);
  }

  public void refresh() {
    // System.out.println("refresh");
    this.repaint();
  }
}