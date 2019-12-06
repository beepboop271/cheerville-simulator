import java.awt.Color;

public class Plant extends Spawnable {
  private static final int INITIAL_HEALTH = 15;
  private static final int HEALTH_VARIANCE = 5;
  private static final int MAX_HEALTH = 25;

  private static final double PLANT_ENERGY_FACTOR = 0.5;

  private static final double SPREAD_CHANCE = 0.05;

  private static long NUM_PLANTS = 0;

  private Plant ancestor;

  public Plant(int x, int y) {
    super(x, y,
          (Plant.INITIAL_HEALTH
           + (int)(((Math.random()-0.5)*2)
                   * (Plant.HEALTH_VARIANCE+1))));
  }

  @Override
  public String toString() {
    return "Plant#"+this.getID();
  }

  @Override
  public int decay() {
    if (Math.random() < 0.5) {
      return super.decay();
    }
    return this.getHealth();
  }

  @Override
  public Spawnable act(Spawnable other) {
    if (other instanceof Plant) {
      // other is only non null when a plant
      // spreads onto another plant
      this.heal((int)(other.getHealth()*Plant.PLANT_ENERGY_FACTOR));
      other.setDead();
      ((Plant)other).ancestor.addDescendant(this);
    } else {
      if (Math.random() < Plant.SPREAD_CHANCE) {
        return this.addDescendantWithAncestor(new Plant(this.getX(), this.getY()));
      }
    }
    return null;
  }

  public Spawnable addDescendantWithAncestor(Plant descendant) {
    descendant.ancestor = this;
    this.addDescendant(descendant);
    return descendant;
  }

  @Override
  public int getMaxHealth() {
    return Plant.MAX_HEALTH;
  }

  @Override
  public int getInitialHealth() {
    return Plant.INITIAL_HEALTH;
  }

  @Override
  public int getHealthVariance() {
    return Plant.HEALTH_VARIANCE;
  }

  @Override
  public Color getColor() {
    return new Color(this.getColorChannelValue(), 255, this.getColorChannelValue());
  }

  @Override
  public long generateID() {
    return Plant.NUM_PLANTS++;
  }
}