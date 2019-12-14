import java.awt.Color;

/**
 * [Zombie]
 * A being that can eat Humans or convert Humans
 * to more Zombies.
 * 2019-12-13
 * @version 2.3
 * @author Kevin Qiao
 */
public class Zombie extends Moveable {
  private static final int DEFAULT_INITIAL_HEALTH = 10;
  private static int initialHealth = Zombie.DEFAULT_INITIAL_HEALTH;

  private static final int DEFAULT_HEALTH_VARIANCE = 5;
  private static int healthVariance = Zombie.DEFAULT_HEALTH_VARIANCE;

  private static final int DEFAULT_MAX_HEALTH = 20;
  private static int maxHealth = Zombie.DEFAULT_MAX_HEALTH;

  private static final double DEFAULT_HUMAN_ENERGY_FACTOR = 0.1;
  private static double humanEnergyFactor = Zombie.DEFAULT_HUMAN_ENERGY_FACTOR;

  private static final double DEFAULT_RANDOM_MOVE_CHANCE = 0.8;
  private static double randomMoveChance = Zombie.DEFAULT_RANDOM_MOVE_CHANCE;


  private static long numZombies = 0;

  
  /** 
   * [Zombie]
   * Constructor for a Zombie with the given position.
   * @param x The x coordinate of the Zombie.
   * @param y The y coordinate of the Zombie.
   */
  public Zombie(int x, int y) {
    super(x, y,
          (Zombie.initialHealth
           + (int)(((Math.random()-0.5)*2)
                   * (Zombie.healthVariance+1))));
  }

  /** 
   * [Zombie]
   * Constructor for a Zombie to convert a Human into
   * a Zombie.
   * @param victim The Human to copy properties from (to convert
   *               to a Zombie)
   */
  public Zombie(Human victim) {
    super(victim.getX(), victim.getY(),
          Math.min(Zombie.maxHealth, victim.getHealth()));
    victim.setDead();
  }


  /** 
   * [toString]
   * @return String
   */
  @Override
  public String toString() {
    return "Zombie#"+this.getID();
  }
  

  /** 
   * [act]
   * Determine how to act when colliding with the given
   * Spawnable.
   * @param other The Spawnable that this collided with.
   * @return Spawnable, either null to indicate this Zombie can
   *         move onto the location of the Spawnable, this to
   *         indicate the Zombie must move back to its original
   *         location, or a new Spawnable to indicate the Zombie
   *         must move back to its original location and create
   *         a new Spawnable on the World it is in.
   */
  @Override
  public Spawnable act(Spawnable other) {
    if (other instanceof Plant) {
      // null = zombie can move to the spot
      // and nothing new spawns
      other.setDead();
      other.addDescendant(this);
      return null;
    } else if (other instanceof Human) {
      // attackHuman -> null = zombie eats it, moves to spot
      // nothing new spawns
      // attackHuman -> Zombie = zombie converts human, doesn't
      // move, and the new zombie replaces the human
      return this.attackHuman((Human)other);
    } else if (other instanceof Zombie) {
      // non null = zombie is prevented from moving
      // set the world at this position to this, i.e. do nothing
      this.turnAround();
      return this;
    }
    // otherwise allow the movement
    return null;
  }

  
  /** 
   * [getVisionValue]
   * Determines how useful the Spawnables visible to this Zombie are
   * by counting the amount of Humans.
   * @return int, the number of Humans visible to this Zombie.
   */
  @Override
  public int getVisionValue() {
    Spawnable[][] vision = this.getVision();
    int value = 0;
    if (vision != null) {
      for (int i = 0; i < vision.length; ++i) {
        for (int j = 0; j < vision[0].length; ++j) {
          if (vision[i][j] instanceof Human) {
            ++value;
          }
        }
      }
    }
    return value;
  }

  
  /** 
   * [getInfluenceVectorFor]
   * Generates a Vector2D object which represents what direction
   * this Zombie should move in to react to a Spawnable, and how much
   * it should be considering moving in that direction (the longer the
   * vector returned, the more influence it has over the final movement).
   * @param other The Spawnable to react to.
   * @return Vector2D, the vector which stores the direction this Zombie
   *         should move in and how important it is to move in that direction.
   */
  @Override
  public Vector2D getInfluenceVectorFor(Spawnable other) {
    Vector2D influence;
    influence = this.getDistanceVectorTo(other);
    if (other instanceof Human) {
      // inversely proportional to zombie's health
      // proportional to the human's health
      // inversely proportional to human's distance
      influence.setLength((10.0/this.getHealth())
                          * other.getHealth()
                          * (5.0/influence.getLength()));
    } else if (other instanceof Zombie) {
      // inversely proportional to zombie's distance
      influence.setLength(3.0/influence.getLength());
      // move away, not towards
      influence.flip();
    } else {
      influence.setLength(0);
    }
    return influence;
  }

  
  /** 
   * [attackHuman]
   * Handles collision with a Human. Either the Human
   * is consumed by the Zombie or the Human is converted
   * to a new Zombie.
   * @param victim The Human to attack.
   * @return Zombie, null if the Human was consumed or a new
   *         Zombie that the Human was converted to.
   */
  public Zombie attackHuman(Human victim) {
    if (this.getHealth() > victim.getHealth()) {
      this.heal((int)(victim.getHealth()*Zombie.humanEnergyFactor));
      victim.setDead();
      return null;
    } else {
      return (Zombie)victim.addDescendant(new Zombie(victim));
    }
  }

  
  /** 
   * [generateID]
   * Generates a unique ID for this Zombie. A Zombie with
   * the ID of n is hte nth Zombie to be created.
   * @return long, the new ID.
   */
  @Override
  public long generateID() {
    return Zombie.numZombies++;
  }

  
  /** 
   * [getMaxHealth]
   * Returns the maximum health a Zombie can have.
   * @return int, the maximum health possible for a Zombie.
   */
  @Override
  public int getMaxHealth() {
    return Zombie.maxHealth;
  }

  
  /** 
   * [getDefaultMaxHealth]
   * Returns the default maximum health a Zombie can have.
   * @return int, the default maximum health possible for a Zombie.
   */
  public static int getDefaultMaxHealth() {
    return Zombie.DEFAULT_MAX_HEALTH;
  }

  
  /** 
   * [setMaxHealth]
   * Sets the maximum health a Zombie can have.
   * @param maxHealth The new maximum health possible for a Zombie.
   */
  public static void setMaxHealth(int maxHealth) {
    Zombie.maxHealth = maxHealth;
  }

  
  /** 
   * [getInitialHealth]
   * Returns the health a new Zombie with unspecified
   * health should have.
   * @return int, the starting health of a Zombie.
   */
  @Override
  public int getInitialHealth() {
    return Zombie.initialHealth;
  }

  
  /** 
   * [getDefaultInitialHealth]
   * Returns the default health a new Zombie with unspecified
   * health should have.
   * @return int, the default starting health of a Zombie.
   */
  public static int getDefaultInitialHealth() {
    return Zombie.DEFAULT_INITIAL_HEALTH;
  }

  
  /** 
   * [setInitialHealth]
   * Sets the default health a new Zombie with unspecified
   * health should have.
   * @param initialHealth The new starting health of a Zombie.
   */
  public static void setInitialHealth(int initialHealth) {
    Zombie.initialHealth = initialHealth;
  }


  /** 
   * [getHealthVariance]
   * Returns the maximum difference between the
   * actual initial health of a Zombie and the
   * default initial health from getInitialHealth().
   * @return int, the maximum variance in initial health
   *         for a Zombie.
   */
  @Override
  public int getHealthVariance() {
    return Zombie.healthVariance;
  }

  
  /** 
   * [getDefaultHealthVariance]
   * Returns the default maximum difference between the
   * actual initial health of a Zombie and the default
   * initial health from getInitialHealth().
   * @return int, the default maximum variance in initial health
   *         for a Zombie.
   */
  public static int getDefaultHealthVariance() {
    return Zombie.DEFAULT_HEALTH_VARIANCE;
  }

  
  /** 
   * [setHealthVariance]
   * Sets the default maximum difference between the actual
   * initial health of a Zombie and the default initial health
   * from getInitialHealth().
   * @param healthVariance The new maximum variance in initial health
   *                       for a Zombie.
   */
  public static void setHealthVariance(int healthVariance) {
    Zombie.healthVariance = healthVariance;
  }

  
  /** 
   * [getRandomMoveChance]
   * Returns the chance [0, 1] a Zombie will move in a
   * randomly chosen direction instead of determining the
   * best movement.
   * @return double, the chance [0, 1] a Zombie will move
   *         randomly.
   */
  @Override
  public double getRandomMoveChance() {
    return Zombie.randomMoveChance;
  }

  
  /** 
   * [getDefaultRandomMoveChance]
   * Returns the default chance [0, 1] a Zombie will move in
   * a randomly chosen direction instead of determining the
   * best movement.
   * @return double, the default chance [0, 1] a Zombie will move
   *         randomly.
   */
  public static double getDefaultRandomMoveChance() {
    return Zombie.DEFAULT_RANDOM_MOVE_CHANCE;
  }

  
  /** 
   * [setRandomMoveChance]
   * Sets the chance [0, 1] a Zombie will move in
   * a randomly chosen direction instead of determining the
   * best movement.
   * @param randomMoveChance The new default chacne [0, 1] a Zombie
   *                         will move randomly.
   */
  public static void setRandomMoveChance(double randomMoveChance) {
    Zombie.randomMoveChance = randomMoveChance;
  }

  
  /** 
   * [getDefaultHumanEnergyFactor]
   * Returns the default factor that a Human's health is
   * multiplied by when calculating how much health it
   * restores.
   * @return double, the factor between a Human's health and
   *         the amount of health that Human restores.
   */
  public static double getDefaultHumanEnergyFactor() {
    return Zombie.DEFAULT_HUMAN_ENERGY_FACTOR;
  }

  
  /** 
   * [setHumanEnergyFactor]
   * Sets the factor that a Human's health is multiplied
   * by when calculating how much health it restores.
   * @param plantEnergyFactor The factor between a Human's health and
   *                          the amount of health that Human restores.
   */
  public static void setHumanEnergyFactor(double humanEnergyFactor) {
    Zombie.humanEnergyFactor = humanEnergyFactor;
  }

  
  /** 
   * [getColor]
   * Returns the Color that this Zombie should be drawn with.
   * The lower the health, the more pale (white) the colour
   * will be. Max health means pure red.
   * @return Color, the Color to represent this Zombie.
   */
  @Override
  public Color getColor() {
    return new Color(255, this.getColorChannelValue(), this.getColorChannelValue());
  }
}