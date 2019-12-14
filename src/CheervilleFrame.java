import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * [CheervilleFrame]
 * JFrame that contains all graphical elements of the
 * Cheerville project.
 * 2019-12-13
 * @version 1.7
 * @author Kevin Qiao
 */
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

    // large WorldPanel which shows the map on the left
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

    // small panel of 3 buttons in the top middle
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

    // graph that shows % composition of the map below the buttons
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

    // graph that shows counts of Male, Female, Zombie below the % graph panel
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
    
    // panel which shows information about a selected Spawnable at the bottom middle
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

    // for some reason, grid bag layout really doesn't like it
    // when i add a JTabbedPane, and i'm not sure how to go about
    // fixing it, so just put the grid bag from above into another
    // grid bag
    JPanel mainPane = new JPanel();
    mainPane.setLayout(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    mainPane.add(gridBagPane, gbc);

    // panel of sliders on the right
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