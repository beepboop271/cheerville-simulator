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

  public int getMinBirthInterval() {
    return Female.MIN_BIRTH_INTERVAL;
  }

  public double getBirthChance() {
    return Human.getChanceByInterpolation(Female.AGE_BIRTH_CHANCES, this.getAge())
           + Human.getChanceByInterpolation(Female.HEALTH_BIRTH_CHANCES, this.getHealth());
  }
}