public class Male extends Human {
  private static final int MIN_BIRTH_INTERVAL = 2;
  private static final double[][] AGE_BIRTH_CHANCES = {
    {0.0, -0.5},
    {10.0, 0.5},
    {20.0, 0.85},
    {40.0, 0.85},
    {60.0, 0.5},
    {100.0, 0.3}
  };
  private static final double[][] HEALTH_BIRTH_CHANCES = {
    {0.0, -0.3},
    {5.0, 0.1},
    {15.0, 0.7},
    {25.0, 0.9},
    {30.0, 1.0}
  };

  public Male(int x, int y) {
    super(x, y);
  }

  public Male(int x, int y, int initialHealth) {
    super(x, y, initialHealth);
  }

  public int[] generateSmartMove() {
    // return this.generateRandomMove();
    Vector2D direction = new Vector2D(0, 0);
    Vector2D influence;
    Spawnable[][] vision = this.getVision();
    Spawnable s;

    for(int i = 0; i < vision.length; ++i) {
      for(int j = 0; j < vision[0].length; ++j) {
        s = vision[i][j];
        if(s != null && s != this) {
          influence = this.getDistanceVectorTo(s);
          if(s instanceof Plant) {
            // inversely proportional to human's health
            // proportional to plant's health
            // inversely proportional to plant's distance
            influence.setLength((10.0/this.getHealth())
                                * (s.getHealth())
                                * (5.0/influence.getLength()));
            direction = direction.add(influence);
          } else if(s instanceof Female) {
            // proportional to male's birth chance
            // proportional to female's birth chance
            // inversely proportional to female's distance
            influence.setLength((4.0*this.getBirthChance())
                                * (2.0*((Female)s).getBirthChance())
                                * (5.0/influence.getLength()));
            direction = direction.add(influence);
          } else if(s instanceof Zombie) {
            // inversely proportional to male's birth chance
            // (lower birth chance = closer to death)
            // inversely proportional to zombie's distance
            // proportional to zombie's health
            influence.setLength((0.5/this.getBirthChance())
                                * (5.0/influence.getLength())
                                * (0.3*s.getHealth()));
            influence.flip();
            direction = direction.add(influence);
          }
        }
      }
    }

    int move = direction.asMoveInteger();
    if(move == 0) {
      return this.generateRandomMove();
    } else {
      int[] deltas = Cheerville.MOVEMENTS[move];
      int[] pos = {this.getX()+deltas[0], this.getY()+deltas[1]};
      this.setFacingDirection(move);
      return pos;
    }
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
}