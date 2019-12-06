import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CheervilleFrame extends JFrame {
  
  /** 
   * [CheervilleFrame]
   * Constructor for a window that holds all the panels
   * for the cheerville program.
   * @param title          The window title.
   * @param worldToDisplay The World object to show in the window.
   */
  public CheervilleFrame(String title, World worldToDisplay) {
    super(title);

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
    WorldPanel worldPane = new WorldPanel(cellSize, worldToDisplay);
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
    mainPane.add(new PercentageGraphPanel(worldToDisplay), c);

    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.6;
    c.weighty = 1.0;
    c.insets = new Insets(0, 0, 10, 10);
    mainPane.add(new CountGraphPanel(worldToDisplay), c);
    
    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 2;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.6;
    c.weighty = 0.9;
    c.insets = new Insets(0, 0, 10, 10);
    worldPane.setInfoPanel(new SpawnableInfoPanel(new SpawnableVisionPanel(20, worldToDisplay, null),
                                                  new SpawnableTextPanel(190, 200),
                                                  new SpawnableButtonPanel(worldToDisplay)));
    mainPane.add(worldPane.getInfoPanel(), c);

    mainPane.setOpaque(true);
    this.setContentPane(mainPane);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
    this.setVisible(true);
  }

  
  /**
   * [refresh]
   * Refreshes the frame.
   */
  public void refresh() {
    this.repaint();
  }
}