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
  public SliderPanel() {
    super();

    JPanel world = new JPanel();
    world.setPreferredSize(new Dimension(300, 400));
    world.setLayout(new BoxLayout(world, BoxLayout.Y_AXIS));

    JSlider[] worldSliders = new JSlider[5];
    
    world.add(new JLabel("Step delay:"));
    worldSliders[0] = new JSlider(0, 500, WorldManager.getDelay());
    worldSliders[0].setName("delay");
    worldSliders[0].setMajorTickSpacing(100);
    world.add(worldSliders[0]);

    world.add(new JLabel("Initial humans:"));
    worldSliders[1] = new JSlider(0, 500, WorldManager.getInitialHumans());
    worldSliders[1].setName("initialHumans");
    worldSliders[1].setMajorTickSpacing(100);
    world.add(worldSliders[1]);

    world.add(new JLabel("Initial zombies:"));
    worldSliders[2] = new JSlider(0, 500, WorldManager.getInitialZombies());
    worldSliders[2].setName("initialZombies");
    worldSliders[2].setMajorTickSpacing(100);
    world.add(worldSliders[2]);

    world.add(new JLabel("Vision range:"));
    worldSliders[3] = new JSlider(1, 10, World.getVisionSize());
    worldSliders[3].setName("visionRange");
    worldSliders[3].setMajorTickSpacing(5);
    world.add(worldSliders[3]);

    world.add(new JLabel("Plant spawn chance:"));
    worldSliders[4] = new JSlider(0, 100, (int)(100*World.getPlantSpawnChance()));
    worldSliders[4].setName("plantSpawnChance");
    worldSliders[4].setMajorTickSpacing(10);
    world.add(worldSliders[4]);

    for(int i = 0; i < worldSliders.length; ++i) {
      worldSliders[i].addChangeListener(this);
      worldSliders[i].setPaintTicks(true);
      worldSliders[i].setPaintLabels(true);
    }

    // JPanel humanSliders = new JPanel();
    // humanSliders.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    // humanSliders.add(new JSlider(0, 100, Human.))

    this.addTab("World", world);
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider)e.getSource();
    if (!source.getValueIsAdjusting()) {
      String name = source.getName();
      if (name.equals("delay")) {
        WorldManager.setDelay(source.getValue());
      } else if (name.equals("initialHumans")) {
        WorldManager.setInitialHumans(source.getValue());
      } else if (name.equals("initialZombies")) {
        WorldManager.setInitialZombies(source.getValue());
      } else if (name.equals("visionRange")) {
        World.setVisionSize(source.getValue());
      } else if (name.equals("plantSpawnChance")) {
        World.setPlantSpawnChance(source.getValue()/100.0);
      }
    }
  }
}