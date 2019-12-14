import java.awt.Color;

public class Plant extends Spawnable {
  private static final int DEFAULT_INITIAL_HEALTH = 15;
  private static int initialHealth = Plant.DEFAULT_INITIAL_HEALTH;

  private static final int DEFAULT_HEALTH_VARIANCE = 5;
  private static int healthVariance = Plant.DEFAULT_HEALTH_VARIANCE;

  private static final int DEFAULT_MAX_HEALTH = 25;
  private static int maxHealth = Plant.DEFAULT_MAX_HEALTH;

  private static final double DEFAULT_PLANT_ENERGY_FACTOR = 0.5;
  private static double plantEnergyFactor = Plant.DEFAULT_PLANT_ENERGY_FACTOR;

  private static final double DEFAULT_SPREAD_CHANCE = 0.05;
  private static double spreadChance = Plant.DEFAULT_SPREAD_CHANCE;

  
  private static long numPlants = 0;

  private Plant ancestor;

  
  /** 
   * [Plant]
   * Constructor for a Plant with the given position.
   * @param x The x coordinate of the Plant.
   * @param y The y coordinate of the Plant.
   */
  public Plant(int x, int y) {
    super(x, y,
          (Plant.initialHealth
           + (int)(((Math.random()-0.5)*2)
                   * (Plant.healthVariance+1))));
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
      this.heal((int)(other.getHealth()*Plant.plantEnergyFactor));
      other.setDead();
      ((Plant)other).ancestor.addDescendant(this);
    } else {
      if (Math.random() < Plant.spreadChance) {
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
    return Plant.maxHealth;
  }

  
  /** 
   * [getDefaultMaxHealth]
   * Returns the default maximum health a Plant can have.
   * @return int, the default maximum health possible for a Plant.
   */
  public static int getDefaultMaxHealth() {
    return DEFAULT_MAX_HEALTH;
  }

  
  /** 
   * [setMaxHealth]
   * Sets the maximum health a Plant can have.
   * @param maxHealth The maximum health possible for a Plant.
   */
  public static void setMaxHealth(int maxHealth) {
    Plant.maxHealth = maxHealth;
  }

  
  /** 
   * [getInitialHealth]
   * Returns the health a new Plant with unspecified
   * health should have.
   * @return int, the starting health of a Plant.
   */
  @Override
  public int getInitialHealth() {
    return Plant.initialHealth;
  }

  
  /** 
   * [getDefaultInitialHealth]
   * Returns the default health a new Plant with unspecified
   * health should have.
   * @return int, the default starting health of a Plant.
   */
  public static int getDefaultInitialHealth() {
    return DEFAULT_INITIAL_HEALTH;
  }

  
  /** 
   * [setInitialHealth]
   * Sets the health a new Plant with unspecified
   * health should have.
   * @param initialHealth The new starting health of a Plant.
   */
  public static void setInitialHealth(int initialHealth) {
    Plant.initialHealth = initialHealth;
  }

  
  /** 
   * [getHealthVariance]
   * Returns the maximum difference between the
   * actual initial health of a Plant and the
   * default initial health from getInitialHealth().
   * @return int, the maximum variance in initial health
   *         for a Plant.
   */
  @Override
  public int getHealthVariance() {
    return Plant.healthVariance;
  }

  
  /** 
   * [getDefaultHealthVariance]
   * Returns the default maximum difference between
   * the actual initial health of a Plant and the default
   * initial health from getInitialHealth().
   * @return int, the default maximum variance in initial
   *         health for a Plant.
   */
  public static int getDefaultHealthVariance() {
    return DEFAULT_HEALTH_VARIANCE;
  }

  
  /** 
   * [setHealthVariance]
   * Sets the maximum difference between the actual initial
   * health of a Plant and the default initial health from
   * getInitialHealth().
   * @param healthVariance The new maximum variance in initial
   *                       health for a Plant.
   */
  public static void setHealthVariance(int healthVariance) {
    Plant.healthVariance = healthVariance;
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
    return Plant.numPlants++;
  }

  
  /** 
   * [getDefaultPlantEnergyFactor]
   * Returns the default factor that a Plant's health is
   * multiplied by when calculating how much health it
   * restores. Used when a Plant spreads onto another.
   * @return double, the factor between a Plant's health and
   *         the amount of health it restores.
   */
  public static double getDefaultPlantEnergyFactor() {
    return DEFAULT_PLANT_ENERGY_FACTOR;
  }

  
  /** 
   * [setPlantEnergyFactor]
   * Sets the factor that a Plant's health is multiplied by
   * when calculating how much health it restores.
   * @param plantEnergyFactor The factor between a Plant's health
   *                          and the amount of health that Plant restores.
   */
  public static void setPlantEnergyFactor(double plantEnergyFactor) {
    Plant.plantEnergyFactor = plantEnergyFactor;
  }

  
  /** 
   * [getDefaultSpreadChance]
   * Returns the default chance [0, 1] that a Plant will spread
   * to produce a new Plant beside it in one simulation step.
   * @return double, the chance a Plant will spread to a new
   *         cell in one simulation step.
   */
  public static double getDefaultSpreadChance() {
    return DEFAULT_SPREAD_CHANCE;
  }

  
  /** 
   * [setSpreadChance]
   * Sets the chance [0, 1] that a Plant will spread to produce
   * a new Plant beside it in one simulation step.
   * @param spreadChance The chance a Plant will spread to a new
   *                     cell in one simulation step.
   */
  public static void setSpreadChance(double spreadChance) {
    Plant.spreadChance = spreadChance;
  }
}