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

public class SliderPanel extends JTabbedPane {
  public SliderPanel() {
    super();

    JPanel humanSliders = new JPanel();
    humanSliders.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    humanSliders.add(new JSlider(0, 100, Human.))
  }
}