import java.awt.Color;

public class Female extends Human {
  private static final int MIN_BIRTH_INTERVAL = 10;
  private static final double[][] AGE_BIRTH_CHANCES = {
    {0.0, 0.0},
    {10.0, 0.1},
    {20.0, 0.45},
    {30.0, 0.6},
    {40.0, 0.45},
    {60.0, 0.2},
    {100.0, 0.1}
  };
  private static final double[][] HEALTH_BIRTH_CHANCES = {
    {0.0, -1.0},
    {5.0, -0.5},
    {15.0, 0.2},
    {25.0, 0.5},
    {30.0, 0.6}
  };

  public Female(int x, int y) {
    super(x, y);
  }

  public Female(int x, int y, int initialHealth) {
    super(x, y, initialHealth);
  }

  @Override
  public int getVisionValue() {
    Spawnable[][] vision = this.getVision();
    int value = 0;
    if (vision != null) {
      for (int i = 0; i < vision.length; ++i) {
        for (int j = 0; j < vision[0].length; ++j) {
          if (vision[i][j] instanceof Plant || vision[i][j] instanceof Male) {
            ++value;
          }
        }
      }
    }
    return value;
  }

  @Override
  public Vector2D getInfluenceVectorFor(Spawnable other) {
    Vector2D influence;
    influence = this.getDistanceVectorTo(other);
    if (other instanceof Plant) {
      // inversely proportional to human's health
      // proportional to plant's health
      // inversely proportional to plant's distance
      influence.setLength((20.0/this.getHealth())
                          * (other.getHealth())
                          * (5.0/influence.getLength()));
    } else if (other instanceof Male) {
      // proportional to female's birth chance
      // proportional to male's birth chance
      // inversely proportional to female's distance
      influence.setLength((2.0*this.getBirthChance())
                          * (2.0*((Male)other).getBirthChance())
                          * (5.0/influence.getLength()));
      if (this.getBirthChance() < 0.5) {
        influence.flip();
      }
    } else if (other instanceof Female) {
      influence.setLength(6.0*(this.getBirthChance()
                               - ((Female)other).getBirthChance()));
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

  @Override
  public int getMinBirthInterval() {
    return Female.MIN_BIRTH_INTERVAL;
  }
  
  @Override
  public double[][] getAgeBirthChances() {
    return Female.AGE_BIRTH_CHANCES;
  }
  
  @Override
  public double[][] getHealthBirthChances() {
    return Female.HEALTH_BIRTH_CHANCES;
  }

  @Override
  public Color getColor() {
    return new Color(255, this.getColorChannelValue(), 255);
  }
}