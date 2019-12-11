import java.awt.Color;

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


  private static long NUM_ZOMBIES = 0;

  
  /** 
   * @param x
   * @param y
   * @return 
   */
  public Zombie(int x, int y) {
    super(x, y,
          (Zombie.initialHealth
           + (int)(((Math.random()-0.5)*2)
                   * (Zombie.healthVariance+1))));
  }

  
  /** 
   * @param victim
   * @return 
   */
  public Zombie(Human victim) {
    super(victim.getX(), victim.getY(),
          Math.min(Zombie.maxHealth, victim.getHealth()));
    victim.setDead();
  }

  
  /** 
   * @param other
   * @return Spawnable
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
   * @return int
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
   * @param other
   * @return Vector2D
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
   * @param victim
   * @return Zombie
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
   * @return String
   */
  @Override
  public String toString() {
    return "Zombie#"+this.getID();
  }

  
  /** 
   * @return int
   */
  @Override
  public int getMaxHealth() {
    return Zombie.maxHealth;
  }

  
  /** 
   * @return int
   */
  @Override
  public int getInitialHealth() {
    return Zombie.initialHealth;
  }

  
  /** 
   * @return int
   */
  @Override
  public int getHealthVariance() {
    return Zombie.healthVariance;
  }

  
  /** 
   * @return double
   */
  @Override
  public double getRandomMoveChance() {
    return Zombie.randomMoveChance;
  }


  public static double getDefaultRandomMoveChance() {
    return Zombie.DEFAULT_RANDOM_MOVE_CHANCE;
  }


  public static void setRandomMoveChance(double randomMoveChance) {
    Zombie.randomMoveChance = randomMoveChance;
  }

  
  /** 
   * @return Color
   */
  @Override
  public Color getColor() {
    return new Color(255, this.getColorChannelValue(), this.getColorChannelValue());
  }

  
  /** 
   * @return long
   */
  @Override
  public long generateID() {
    return Zombie.NUM_ZOMBIES++;
  }

  public static int getDefaultInitialHealth() {
    return Zombie.DEFAULT_INITIAL_HEALTH;
  }

  public static void setInitialHealth(int initialHealth) {
    Zombie.initialHealth = initialHealth;
  }

  public static int getDefaultHealthVariance() {
    return Zombie.DEFAULT_HEALTH_VARIANCE;
  }

  public static void setHealthVariance(int healthVariance) {
    Zombie.healthVariance = healthVariance;
  }

  public static int getDefaultMaxHealth() {
    return Zombie.DEFAULT_MAX_HEALTH;
  }

  public static void setMaxHealth(int maxHealth) {
    Zombie.maxHealth = maxHealth;
  }

  public static double getDefaultHumanEnergyFactor() {
    return Zombie.DEFAULT_HUMAN_ENERGY_FACTOR;
  }

  public static double getHumanEnergyFactor() {
    return Zombie.humanEnergyFactor;
  }

  public static void setHumanEnergyFactor(double humanEnergyFactor) {
    Zombie.humanEnergyFactor = humanEnergyFactor;
  }
}