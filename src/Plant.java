import java.awt.Color;

public class Plant extends Spawnable {
  private static final int INITIAL_HEALTH = 15;
  private static final int HEALTH_VARIANCE = 5;
  private static final int MAX_HEALTH = 25;

  private static final double PLANT_ENERGY_FACTOR = 0.5;

  private static final double SPREAD_CHANCE = 0.05;

  private static long NUM_PLANTS = 0;

  private Plant ancestor;

  
  /** 
   * [Plant]
   * Constructor for a Plant with the given position.
   * @param x The x coordinate of the Plant.
   * @param y The y coordinate of the Plant.
   */
  public Plant(int x, int y) {
    super(x, y,
          (Plant.INITIAL_HEALTH
           + (int)(((Math.random()-0.5)*2)
                   * (Plant.HEALTH_VARIANCE+1))));
  }

  
  /** 
   * [toString]
   * Generates a String representation of the Plant
   * using its ID.
   * @return String, the representation of this Plant.
   */
  @Override
  public String toString() {
    return "Plant#"+this.getID();
  }

  
  /** 
   * [decay]
   * Reduces and returns this Plant's health. Plants
   * have the chance to not decay unlike other Spawnables.
   * @return int
   */
  @Override
  public int decay() {
    if (Math.random() < 0.5) {
      return super.decay();
    }
    return this.getHealth();
  }

  
  /** 
   * [act]
   * Determine how to act when colliding with the given
   * Spawnable. Plants can spread onto other Plants which
   * triggers the act method, or Plants can act by spreading.
   * @param other The Spawnable to react to.
   * @return Spawnable, either null to indicate nothing in
   *         the World changed, or a new Plant to spread in
   *         the World.
   */
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

  
  /** 
   * [addDescendantWithAncestor]
   * Adds a descendant to this Plant but also sets the
   * ancestor of the descendant to this. Need to keep track
   * of ancestors due to how Plants spread, the new Plant is
   * "absorbed" and killed by the Plant it moves onto, so
   * the absorbed Plant needs to change the descendant of
   * the ancestor.
   * @param descendant
   * @return Spawnable
   */
  public Spawnable addDescendantWithAncestor(Plant descendant) {
    descendant.ancestor = this;
    this.addDescendant(descendant);
    return descendant;
  }

  
  /** 
   * [getMaxHealth]
   * Returns the maximum health a Plant can have.
   * @return int, the maximum health possible for a Plant.
   */
  @Override
  public int getMaxHealth() {
    return Plant.MAX_HEALTH;
  }

  
  /** 
   * [getInitialHealth]
   * Returns the health a new Plant with unspecified
   * health should have.
   * @return int, the default starting health of a Plant.
   */
  @Override
  public int getInitialHealth() {
    return Plant.INITIAL_HEALTH;
  }

  
  /** 
   * [getHealthVariance]
   * Returns the maximum difference between the
   * actual initial health of a Plant and the
   * default initial health from getInitialHealth()
   * @return int, the maximum variance in initial health
   *         for a Plant.
   */
  @Override
  public int getHealthVariance() {
    return Plant.HEALTH_VARIANCE;
  }

  
  /** 
   * [getColor]
   * Returns the Color that this Plant should be drawn with.
   * The lower the health, the more pale (white) the colour
   * will be. Max health means pure green.
   * @return Color, the Color to represent this Plant.
   */
  @Override
  public Color getColor() {
    return new Color(this.getColorChannelValue(), 255, this.getColorChannelValue());
  }

  
  /** 
   * [generateID]
   * Generates a unique ID for this Plant. A Plant with
   * the ID of n is the nth Plant to be created.
   * @return long
   */
  @Override
  public long generateID() {
    return Plant.NUM_PLANTS++;
  }
}