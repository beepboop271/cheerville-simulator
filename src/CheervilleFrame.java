import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CheervilleFrame extends JFrame {
  private WorldPanel worldPane;
  private PercentageGraphPanel percentGraphPane;

  
  /** 
   * [CheervilleFrame]
   * Constructor for a window that holds all the panels
   * for the cheerville program.
   * @param title          The window title.
   * @param worldToDisplay The World object to show in the window.
   */
  public CheervilleFrame(String title, CheervilleManager managerToDisplay) {
    super(title);

    World worldToDisplay = managerToDisplay.getWorldToRun();

    JPanel gridBagPane = new JPanel();
    gridBagPane.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 4;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 0.4;
    gbc.weighty = 1.0;
    gbc.insets = new Insets(10, 10, 10, 10);
    int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    int cellSize = (int)((screenHeight*0.7)/worldToDisplay.getHeight());
    this.worldPane = new WorldPanel(cellSize, worldToDisplay);
    gridBagPane.add(this.worldPane, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 0.6;
    gbc.weighty = 1.0;
    gbc.insets = new Insets(0, 0, 0, 10);
    SliderPanel sliders = new SliderPanel(managerToDisplay);
    gridBagPane.add(new RunButtonPanel(managerToDisplay, sliders));

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 0.6;
    gbc.weighty = 0.8;
    gbc.insets = new Insets(5, 0, 10, 10);
    this.percentGraphPane = new PercentageGraphPanel(worldToDisplay);
    gridBagPane.add(this.percentGraphPane, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 0.6;
    gbc.weighty = 0.8;
    gbc.insets = new Insets(0, 0, 10, 10);
    gridBagPane.add(new CountGraphPanel(worldToDisplay), gbc);
    
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 0.6;
    gbc.weighty = 0.9;
    gbc.insets = new Insets(0, 0, 10, 10);
    worldPane.setInfoPanel(new SpawnableInfoPanel(new SpawnableVisionPanel(20, worldToDisplay, null),
                                                  new SpawnableTextPanel(190, 200),
                                                  new SpawnableButtonPanel(worldToDisplay)));
    gridBagPane.add(worldPane.getInfoPanel(), gbc);

    gridBagPane.setOpaque(true);

    JPanel mainPane = new JPanel();
    mainPane.setLayout(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    mainPane.add(gridBagPane, gbc);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 0, 10);
    mainPane.add(sliders, gbc);

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

  
  /** 
   * [updateWorldSize]
   * Updates the display when the size of the simulation
   * World changes.
   * @param width  The new width, in number of cells
   * @param height The new height, in number of cells
   */
  public void updateWorldSize(int width, int height) {
    this.worldPane.onResize();
    this.percentGraphPane.setTotalSpaces(width*height);
  }
}