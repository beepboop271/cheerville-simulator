public class Female extends Human {
  private static final int MIN_BIRTH_INTERVAL = 10;
  private static final double[][] AGE_BIRTH_CHANCES = {
    {0.0, 0.0},
    {10.0, 0.1},
    {20.0, 0.5},
    {30.0, 1},
    {40.0, 0.5},
    {60.0, 0.2},
    {100.0, 0.1}
  };
  private static final double[][] HEALTH_BIRTH_CHANCES = {
    {0.0, -0.5},
    {5.0, 0.1},
    {15.0, 0.6},
    {25.0, 0.75},
    {30.0, 0.9}
  };

  public Female(int x, int y) {
    super(x, y);
  }

  public Female(int x, int y, int initialHealth) {
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
          } else if(s instanceof Male) {
            // proportional to female's birth chance
            // proportional to male's birth chance
            // inversely proportional to female's distance
            influence.setLength((2.0*this.getBirthChance())
                                * (3.0*((Male)s).getBirthChance())
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
    return Female.MIN_BIRTH_INTERVAL;
  }
  
  public double[][] getAgeBirthChances() {
    return Female.AGE_BIRTH_CHANCES;
  }
  
  public double[][] getHealthBirthChances() {
    return Female.HEALTH_BIRTH_CHANCES;
  }
}