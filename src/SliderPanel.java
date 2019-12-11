import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderPanel extends JTabbedPane implements ChangeListener {
  public static final int NUM_SLIDERS = 15;
  private JSlider[] sliders = new JSlider[NUM_SLIDERS];
  private int[] defaultValues = new int[NUM_SLIDERS];

  public SliderPanel() {
    super();
    this.setPreferredSize(new Dimension(300, 400));
    int i = 0;

    JPanel worldTab = new JPanel();
    worldTab.setLayout(new BoxLayout(worldTab, BoxLayout.Y_AXIS));
    
    this.addSlider(worldTab, "Step delay:", "worldDelay", i++,
                   0, 500, WorldManager.getDefaultDelay(), 100);
    this.addSlider(worldTab, "Initial humans:", "worldInitialHumans", i++,
                   0, 500, WorldManager.getDefaultInitialHumans(), 100);
    this.addSlider(worldTab, "Initial zombies:", "worldInitialZombies", i++,
                   0, 500, WorldManager.getDefaultInitialZombies(), 100);
    this.addSlider(worldTab, "Vision range:", "worldVisionRange", i++,
                   1, 10, World.getDefaultVisionSize(), 1);
    this.addSlider(worldTab, "Plant spawn chance:", "worldPlantSpawnChance", i++,
                   0, 100, (int)(100*World.getDefaultPlantSpawnChance()), 10);
    this.addTab("World", worldTab);

    JPanel zombieTab = new JPanel();
    zombieTab.setLayout(new BoxLayout(zombieTab, BoxLayout.Y_AXIS));

    this.addSlider(zombieTab, "Initial health:", "zombieInitialHealth", i++,
                   5, 100, Zombie.getDefaultInitialHealth(), 10);
    this.addSlider(zombieTab, "Health variance:", "zombieHealthVariance", i++,
                   0, 20, Zombie.getDefaultHealthVariance(), 5);
    this.addSlider(zombieTab, "Max health:", "zombieMaxHealth", i++,
                   5, 100, Zombie.getDefaultMaxHealth(), 10);
    this.addSlider(zombieTab, "Human energy factor %:", "zombieHumanEnergyFactor", i++,
                   0, 200, (int)(Zombie.getDefaultHumanEnergyFactor()*100), 25);
    this.addSlider(zombieTab, "Random move chance %:", "zombieRandomMoveChance", i++,
                   0, 100, (int)(100*Zombie.getDefaultRandomMoveChance()), 10);
    this.addTab("Zombie", zombieTab);

    JPanel plantTab = new JPanel();
    plantTab.setLayout(new BoxLayout(plantTab, BoxLayout.Y_AXIS));

    this.addSlider(plantTab, "Initial health:", "plantInitialHealth", i++,
                   5, 100, Plant.getDefaultInitialHealth(), 10);
    this.addSlider(plantTab, "Health variance:", "plantHealthVariance", i++,
                   0, 20, Plant.getDefaultHealthVariance(), 5);
    this.addSlider(plantTab, "Max health:", "plantMaxHealth", i++,
                   5, 100, Plant.getDefaultMaxHealth(), 10);
    this.addSlider(plantTab, "Plant energy factor %:", "plantPlantEnergyFactor", i++,
                   0, 200, (int)(Plant.getDefaultPlantEnergyFactor()*100), 25);
    this.addSlider(plantTab, "Spread chance %:", "plantSpreadChance", i++,
                   0, 100, (int)(100*Plant.getDefaultSpreadChance()), 10);
    this.addTab("Plant", plantTab);

    for(i = 0; i < this.sliders.length; ++i) {
      this.defaultValues[i] = this.sliders[i].getValue();
      this.sliders[i].addChangeListener(this);
      this.sliders[i].setPaintTicks(true);
      this.sliders[i].setPaintLabels(true);
    }
  }

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

  @Override
  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider)e.getSource();
    if (!source.getValueIsAdjusting()) {
      String name = source.getName();
      if (name.equals("worldDelay")) {
        WorldManager.setDelay(source.getValue());
      } else if (name.equals("worldInitialHumans")) {
        WorldManager.setInitialHumans(source.getValue());
      } else if (name.equals("worldInitialZombies")) {
        WorldManager.setInitialZombies(source.getValue());
      } else if (name.equals("worldVisionRange")) {
        World.setVisionSize(source.getValue());
      } else if (name.equals("worldPlantSpawnChance")) {
        World.setPlantSpawnChance(source.getValue()/100.0);
      } else if (name.equals("zombieInitialHealth")) {
        Zombie.setInitialHealth(source.getValue());
      } else if (name.equals("zombieHealthVariance")) {
        Zombie.setHealthVariance(source.getValue());
      } else if (name.equals("zombieMaxHealth")) {
        Zombie.setMaxHealth(source.getValue());
      } else if (name.equals("zombieHumanEnergyFactor")) {
        Zombie.setHumanEnergyFactor(source.getValue()/100.0);
      } else if (name.equals("zombieRandomMoveChance")) {
        Zombie.setRandomMoveChance(source.getValue()/100.0);
      } else if (name.equals("plantInitialHealth")) {
        Plant.setInitialHealth(source.getValue());
      } else if (name.equals("plantHealthVariance")) {
        Plant.setHealthVariance(source.getValue());
      } else if (name.equals("plantMaxHealth")) {
        Plant.setMaxHealth(source.getValue());
      } else if (name.equals("plantPlantEnergyFactor")) {
        Plant.setPlantEnergyFactor(source.getValue()/100.0);
      } else if (name.equals("plantSpreadChance")) {
        Plant.setSpreadChance(source.getValue()/100.0);
      }
    }
  }

  public void reset() {
    for(int i = 0; i < this.sliders.length; ++i) {
      this.sliders[i].setValue(this.defaultValues[i]);
    }
  }
}