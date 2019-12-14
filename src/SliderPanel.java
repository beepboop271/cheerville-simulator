import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class SliderPanel extends JTabbedPane implements ChangeListener {
  public static final int NUM_SLIDERS = 23;
  private JSlider[] sliders = new JSlider[NUM_SLIDERS];
  private int[] defaultValues = new int[NUM_SLIDERS];
  private CheervilleManager manager;

  
  /** 
   * [SliderPanel]
   * Constructor for a JPanel with sliders to control settings.
   * @param manager The simulation manager to control some settings with.
   */
  public SliderPanel(CheervilleManager manager) {
    super();
    this.manager = manager;
    this.setPreferredSize(new Dimension(300, 600));
    int i = 0;

    JPanel worldTab = new JPanel();
    worldTab.setLayout(new BoxLayout(worldTab, BoxLayout.Y_AXIS));
    
    this.addSlider(worldTab, "Step delay:", "World delay", i++,
                   0, 500, CheervilleManager.getDefaultDelay(), 100);
    this.addSlider(worldTab, "Width:", "World width", i++,
                   5, 50, CheervilleManager.getDefaultWidth(), 5);
    this.addSlider(worldTab, "Height:", "World height", i++,
                   5, 50, CheervilleManager.getDefaultHeight(), 5);
    this.addSlider(worldTab, "Initial humans:", "World initialHumans", i++,
                   0, 500, CheervilleManager.getDefaultInitialHumans(), 100);
    this.addSlider(worldTab, "Initial zombies:", "World initialZombies", i++,
                   0, 500, CheervilleManager.getDefaultInitialZombies(), 100);
    this.addSlider(worldTab, "Vision range:", "World visionRange", i++,
                   1, 10, World.getDefaultVisionSize(), 1);
    this.addSlider(worldTab, "Plant spawn chance:", "World plantSpawnChance", i++,
                   0, 100, (int)(World.getDefaultPlantSpawnChance()*100), 10);

    this.addTab("World", worldTab);

    JPanel zombieTab = new JPanel();
    zombieTab.setLayout(new BoxLayout(zombieTab, BoxLayout.Y_AXIS));

    this.addSlider(zombieTab, "Initial health:", "Zombie initialHealth", i++,
                   5, 100, Zombie.getDefaultInitialHealth(), 10);
    this.addSlider(zombieTab, "Health variance:", "Zombie healthVariance", i++,
                   0, 20, Zombie.getDefaultHealthVariance(), 5);
    this.addSlider(zombieTab, "Max health:", "Zombie maxHealth", i++,
                   5, 100, Zombie.getDefaultMaxHealth(), 10);
    this.addSlider(zombieTab, "Human energy factor %:", "Zombie humanEnergyFactor", i++,
                   0, 200, (int)(Zombie.getDefaultHumanEnergyFactor()*100), 25);
    this.addSlider(zombieTab, "Random move chance %:", "Zombie randomMoveChance", i++,
                   0, 100, (int)(Zombie.getDefaultRandomMoveChance()*100), 10);

    this.addTab("Zombie", zombieTab);

    JPanel plantTab = new JPanel();
    plantTab.setLayout(new BoxLayout(plantTab, BoxLayout.Y_AXIS));

    this.addSlider(plantTab, "Initial health:", "Plant initialHealth", i++,
                   5, 100, Plant.getDefaultInitialHealth(), 10);
    this.addSlider(plantTab, "Health variance:", "Plant healthVariance", i++,
                   0, 20, Plant.getDefaultHealthVariance(), 5);
    this.addSlider(plantTab, "Max health:", "Plant maxHealth", i++,
                   5, 100, Plant.getDefaultMaxHealth(), 10);
    this.addSlider(plantTab, "Plant energy factor %:", "Plant plantEnergyFactor", i++,
                   0, 200, (int)(Plant.getDefaultPlantEnergyFactor()*100), 25);
    this.addSlider(plantTab, "Spread chance %:", "Plant spreadChance", i++,
                   0, 100, (int)(Plant.getDefaultSpreadChance()*100), 10);

    this.addTab("Plant", plantTab);

    JPanel humanTab = new JPanel();
    humanTab.setLayout(new BoxLayout(humanTab, BoxLayout.Y_AXIS));

    this.addSlider(humanTab, "Initial health:", "Human initialHealth", i++,
                   5, 100, Human.getDefaultInitialHealth(), 10);
    this.addSlider(humanTab, "Health variance:", "Human healthVariance", i++,
                   0, 20, Human.getDefaultHealthVariance(), 5);
    this.addSlider(humanTab, "Max health:", "Human maxHealth", i++,
                   5, 100, Human.getDefaultMaxHealth(), 10);
    this.addSlider(humanTab, "Female:Male ratio:", "Human femaleChance", i++,
                   0, 100, (int)(Human.getDefaultFemaleChance()*100), 10);
    this.addSlider(humanTab, "Plant energy factor %:", "Human plantEnergyFactor", i++,
                   0, 200, (int)(Human.getDefaultPlantEnergyFactor()*100), 25);
    this.addSlider(humanTab, "Random move chance %:", "Human randomMoveChance", i++,
                   0, 100, (int)(Human.getDefaultRandomMoveChance()*100), 25);

    this.addTab("Human", humanTab);

    for(i = 0; i < this.sliders.length; ++i) {
      this.defaultValues[i] = this.sliders[i].getValue();
      this.sliders[i].addChangeListener(this);
      this.sliders[i].setPaintTicks(true);
      this.sliders[i].setPaintLabels(true);
    }
  }

  
  /** 
   * [addSlider]
   * Creates a new JLabel and JSlider for a panel and
   * records it in the JSlider array.
   * @param panelToAdd  The JPanel to add the JLable and JSlider to.
   * @param label       The String to display on the GUI above the JSlider.
   * @param name        The name of the JSlider to use internally.
   * @param index       The index in the slider array to place the new JSlider.
   * @param min         The minimum value of the JSlider.
   * @param max         The maximum value of the JSlider.
   * @param start       The initial value of the JSlider.
   * @param tickSpacing The spacing to place labels at.
   */
  public void addSlider(JPanel panelToAdd,
                        String label, String name,
                        int index,
                        int min, int max, int start,
                        int tickSpacing) {
    panelToAdd.add(new JLabel(label));
    this.sliders[index] = new JSlider(min, max, start);
    this.sliders[index].setName(name);
    this.sliders[index].setMajorTickSpacing(tickSpacing);
    panelToAdd.add(this.sliders[index]);
  }

  
  /** 
   * [stateChanged]
   * Runs when a ChangeEvent on a JSlider is emitted.
   * Checks to see which JSlider has changed and calls
   * the correct setter to update the setting.
   * @param e The ChangeEvent emitted.
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider)e.getSource();
    if (!source.getValueIsAdjusting()) {
      // using reflection is probably a bad idea so
      // 20 switch cases it is

      // https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
      // "The String in the switch expression is compared with the
      // expressions associated with each case label as if the
      // String.equals method were being used."
      switch (source.getName()) {
        case "World delay":
          CheervilleManager.setDelay(source.getValue());
          break;
        case "World width":
          this.manager.setWidth(source.getValue());
          break;
        case "World height":
          this.manager.setHeight(source.getValue());
          break;
        case "World initialHumans":
          CheervilleManager.setInitialHumans(source.getValue());
          break;
        case "World initialZombies":
          CheervilleManager.setInitialZombies(source.getValue());
          break;
        case "World visionRange":
          World.setVisionSize(source.getValue());
          break;
        case "World plantSpawnChance":
          World.setPlantSpawnChance(source.getValue()/100.0);
          break;
        case "Zombie initialHealth":
          Zombie.setInitialHealth(source.getValue());
          break;
        case "Zombie healthVariance":
          Zombie.setHealthVariance(source.getValue());
          break;
        case "Zombie maxHealth":
          Zombie.setMaxHealth(source.getValue());
          break;
        case "Zombie humanEnergyFactor":
          Zombie.setHumanEnergyFactor(source.getValue()/100.0);
          break;
        case "Zombie randomMoveChance":
          Zombie.setRandomMoveChance(source.getValue()/100.0);
          break;
        case "Plant initialHealth":
          Plant.setInitialHealth(source.getValue());
          break;
        case "Plant healthVariance":
          Plant.setHealthVariance(source.getValue());
          break;
        case "Plant maxHealth":
          Plant.setMaxHealth(source.getValue());
          break;
        case "Plant plantEnergyFactor":
          Plant.setPlantEnergyFactor(source.getValue()/100.0);
          break;
        case "Plant spreadChance":
          Plant.setSpreadChance(source.getValue()/100.0);
          break;
        case "Human initialHealth":
          Human.setInitialHealth(source.getValue());
          break;
        case "Human healthVariance":
          Human.setHealthVariance(source.getValue());
          break;
        case "Human maxHealth":
          Human.setMaxHealth(source.getValue());
          break;
        case "Human femaleChance":
          Human.setFemaleChance(source.getValue()/100.0);
          break;
        case "Human plantEnergyFactor":
          Human.setPlantEnergyFactor(source.getValue()/100.0);
          break;
        case "Human randomMoveChance":
          Human.setRandomMoveChance(source.getValue()/100.0);
          break;
      }
    }
  }


  /**
   * [reset]
   * Resets all sliders to their starting values.
   */
  public void reset() {
    for(int i = 0; i < this.sliders.length; ++i) {
      this.sliders[i].setValue(this.defaultValues[i]);
    }
  }
}