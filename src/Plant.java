import java.awt.Color;

public class Plant extends Spawnable {
  private static final int INITIAL_HEALTH = 15;
  private static final int HEALTH_VARIANCE = 5;
  private static final int MAX_HEALTH = 25;

  private static final double PLANT_ENERGY_FACTOR = 0.75;

  private static final double SPREAD_CHANCE = 0.25;

  private Plant ancestor;

  public Plant(int x, int y) {
    super(x, y,
          (Plant.INITIAL_HEALTH
           + (int)(((Math.random()-0.5)*2)  // [-1, 1) * (variance+1)
                   * (Plant.HEALTH_VARIANCE+1))));
  }

  @Override
  public String toString() {
    return "Plant#"+this.getID();
  }

  @Override
  public int decay() {
    if(Math.random() < 0.5) {
      return super.decay();
    }
    return this.getHealth();
  }

  // @Override
  // public Spawnable addDescendant(Spawnable descendant) {
  //   if(this == descendant) {
  //     return descendant;
  //   }
  //   super.addDescendant(descendant);
  //   if(descendant instanceof Plant) {
  //     ((Plant)descendant).ancestor = this;
  //   }
  //   return descendant;
  // }

  public Spawnable addDescendantWithAncestor(Plant descendant) {
    descendant.ancestor = this;
    this.addDescendant(descendant);
    return descendant;
  }

  public Spawnable act(Spawnable other) {
    if(other instanceof Plant) {// && other != this) {
      // other is only non null when a plant
      // spreads onto another plant
      this.heal((int)(other.getHealth()*Plant.PLANT_ENERGY_FACTOR));
      other.setDead();
      ((Plant)other).ancestor.addDescendant(this);
      // if(((Plant)other).ancestor != null) {
      //   ((Plant)other).ancestor.addDescendant(this);
      // }
    } else {
      if(Math.random() < Plant.SPREAD_CHANCE) {
        return this.addDescendantWithAncestor(new Plant(this.getX(), this.getY()));
        // return this.addDescendant(new Plant(this.getX(), this.getY()));
        // this.getContainingWorld().spawnPlantNear(this.getX(), this.getY());
      }
    }
    return null;
  }

  public int getMaxHealth() {
    return Plant.MAX_HEALTH;
  }

  public int getInitialHealth() {
    return Plant.INITIAL_HEALTH;
  }

  public int getHealthVariance() {
    return Plant.HEALTH_VARIANCE;
  }

  public Color getColor() {
    return new Color(this.getColorChannelValue(), 255, this.getColorChannelValue());
  }
}