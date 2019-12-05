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

  public Male(int x, int y) {
    super(x, y);
  }

  public Male(int x, int y, int initialHealth) {
    super(x, y, initialHealth);
  }

  public int getVisionValue() {
    Spawnable[][] vision = this.getVision();
    int value = 0;
    if(vision != null) {
      for(int i = 0; i < vision.length; ++i) {
        for(int j = 0; j < vision[0].length; ++j) {
          if(vision[i][j] instanceof Plant || vision[i][j] instanceof Female) {
            ++value;
          }
        }
      }
    }
    return value;
  }

  public Vector2D getInfluenceVectorFor(Spawnable other) {
    Vector2D influence;
    influence = this.getDistanceVectorTo(other);
    if(other instanceof Plant) {
      // inversely proportional to human's health
      // proportional to plant's health
      // inversely proportional to plant's distance
      influence.setLength((15.0/this.getHealth())
                          * (other.getHealth())
                          * (5.0/influence.getLength()));
    } else if(other instanceof Female) {
      // proportional to male's birth chance
      // proportional to female's birth chance
      // inversely proportional to female's distance
      influence.setLength((3.0*this.getBirthChance())
                          * (2.0*((Female)other).getBirthChance())
                          * (5.0/influence.getLength()));
      if(this.getBirthChance() < 0.5) {
        influence.flip();
      }
    } else if(other instanceof Male) {
      // proportional to difference in birth chances
      influence.setLength(4.0*(this.getBirthChance()
                               - ((Male)other).getBirthChance()));
    } else if(other instanceof Zombie) {
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

  public int getMinBirthInterval() {
    return Male.MIN_BIRTH_INTERVAL;
  }
  
  public double[][] getAgeBirthChances() {
    return Male.AGE_BIRTH_CHANCES;
  }
  
  public double[][] getHealthBirthChances() {
    return Male.HEALTH_BIRTH_CHANCES;
  }

  public Color getColor() {
    return new Color(this.getColorChannelValue(), this.getColorChannelValue(), 255);
  }
}