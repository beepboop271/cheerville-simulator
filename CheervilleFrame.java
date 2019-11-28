import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
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
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.4;
    c.weighty = 1.0;
    c.insets = new Insets(10, 10, 10, 10);
    int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    int cellSize = (int)((screenHeight*0.7)/worldToDisplay.getHeight());
    WorldPanel worldPane = new WorldPanel(cellSize*worldToDisplay.getWidth()+1,
                                          cellSize*worldToDisplay.getHeight()+1,
                                          cellSize,
                                          worldToDisplay);
    mainPane.add(worldPane, c);

    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.6;
    c.weighty = 1.0;
    c.insets = new Insets(10, 0, 10, 10);
    mainPane.add(new PercentageGraphPanel(worldToDisplay.getHistoryAmount(),
                                          worldToDisplay,
                                          worldToDisplay.getWidth()*worldToDisplay.getHeight()),
                 c);

    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.6;
    c.weighty = 1.0;
    c.insets = new Insets(0, 0, 10, 10);
    mainPane.add(new CountGraphPanel(worldToDisplay.getHistoryAmount(),
                                     worldToDisplay),
                 c);
    
    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 2;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.6;
    c.insets = new Insets(0, 0, 10, 10);
    worldPane.setInfoPanel(new SpawnableInfoPanel(worldToDisplay.getHistoryAmount()+60, 300));
    mainPane.add(worldPane.getInfoPanel(), c);

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