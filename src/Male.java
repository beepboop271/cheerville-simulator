import java.awt.Color;

public class Male extends Human {
  private static final int MIN_BIRTH_INTERVAL = 2;
  private static final double[][] AGE_BIRTH_CHANCES = {
    {0.0, -0.7},
    {10.0, 0.1},
    {20.0, 0.6},
    {40.0, 0.7},
    {60.0, 0.6},
    {100.0, 0.2}
  };
  private static final double[][] HEALTH_BIRTH_CHANCES = {
    {0.0, -1.0},
    {5.0, -0.2},
    {15.0, 0.4},
    {25.0, 0.5},
    {30.0, 0.7}
  };

  
  /** 
   * [Male]
   * Constructor for a male Human with the given position.
   * @param x The x coordinate of the Male.
   * @param y The y coordinate of the Male.
   */
  public Male(int x, int y) {
    super(x, y);
  }

  /** 
   * [Male]
   * Constructor for a male Human with the given position and health.
   * @param x             The x coordinate of the Male.
   * @param y             The y coordinate of the Male.
   * @param initialHealth The health of the Male.
   */
  public Male(int x, int y, int initialHealth) {
    super(x, y, initialHealth);
  }

  
  /** 
   * [getVisionValue]
   * Determines how useful the Spawnables visible to this Male are
   * by counting the amount of Plants and Females.
   * @return int, the number of Plants and Females visible to this Male.
   */
  @Override
  public int getVisionValue() {
    Spawnable[][] vision = this.getVision();
    int value = 0;
    if (vision != null) {
      for (int i = 0; i < vision.length; ++i) {
        for (int j = 0; j < vision[0].length; ++j) {
          if ((vision[i][j] instanceof Plant) || (vision[i][j] instanceof Female)) {
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
   * this Male should move in to react to a Spawnable, and how much
   * it should be considering moving in that direction (the longer the
   * vector returned, the more influence it has over the final movement).
   * @param other The Spawnable to react to.
   * @return Vector2D, the vector which stores the direction this Male
   *         should move in and how important it is to move in that direction.
   */
  @Override
  public Vector2D getInfluenceVectorFor(Spawnable other) {
    Vector2D influence;
    influence = this.getDistanceVectorTo(other);
    if (other instanceof Plant) {
      // inversely proportional to human's health
      // proportional to plant's health
      // inversely proportional to plant's distance
      influence.setLength((15.0/this.getHealth())
                          * (other.getHealth())
                          * (5.0/influence.getLength()));
    } else if (other instanceof Female) {
      // proportional to male's birth chance
      // proportional to female's birth chance
      // inversely proportional to female's distance
      influence.setLength((3.0*this.getBirthChance())
                          * (2.0*((Female)other).getBirthChance())
                          * (5.0/influence.getLength()));
      if (this.getBirthChance() < 0.5) {
        influence.flip();
      }
    } else if (other instanceof Male) {
      // proportional to difference in birth chances
      influence.setLength(4.0*(this.getBirthChance()
                               - ((Male)other).getBirthChance()));
    } else if (other instanceof Zombie) {
      // inversely proportional to male's birth chance
      // (lower birth chance = closer to death)
      // inversely proportional to zombie's distance
      // proportional to zombie's health
      influence.setLength((0.5/this.getBirthChance())
                          * (5.0/influence.getLength())
                          * (0.3*other.getHealth()));
      influence.flip();
    } else {
      influence.setLength(0);
    }
    return influence;
  }

  
  /** 
   * [getMinBirthInterval]
   * Returns how many steps a Male needs to take
   * before it can reproduce again.
   * @return int, the number of steps required.
   */
  @Override
  public int getMinBirthInterval() {
    return Male.MIN_BIRTH_INTERVAL;
  }
  
  
  /** 
   * [getAgeBirthChances]
   * Returns the chances a Male can reproduce at certain
   * ages.
   * @return double[][], the array which stores [age, chance]
   *         pairs for reproduction chance at certain ages.
   */
  @Override
  public double[][] getAgeBirthChances() {
    return Male.AGE_BIRTH_CHANCES;
  }
  
  
  /** 
   * [getHealthBirthChances]
   * Returns the chances a Male can reproduce at certain
   * health levels.
   * @return double[][], the array which stores [health, chance]
   *         pairs for reproduction chance at certain health levels.
   */
  @Override
  public double[][] getHealthBirthChances() {
    return Male.HEALTH_BIRTH_CHANCES;
  }

  
  /** 
   * [getColor]
   * Returns the Color that this Male should be drawn with.
   * The lower the health, the more pale (white) the colour
   * will be. Max health means pure blue.
   * @return Color, the Color to represent this Male.
   */
  @Override
  public Color getColor() {
    return new Color(this.getColorChannelValue(), this.getColorChannelValue(), 255);
  }
}