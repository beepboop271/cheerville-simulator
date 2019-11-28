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

  // public Human tryReproduceWith(Human other) {
  //   if ((other instanceof Female)
  //         && this.canReproduce()
  //         && other.canReproduce()) {
  //     // this.getContainingWorld()
  //     //     .spawnHumanNear(this.getX(), this.getY(),
  //     //                     (this.getHealth()+other.getHealth())/2);
  //     this.setStepsUntilFertile(Male.MIN_BIRTH_INTERVAL);
  //     return Human.createHuman(this.getX(), this.getY(),
  //                              (this.getHealth()+other.getHealth())/2);
  //   }
  //   return null;
  // }

  public int getMinBirthInterval() {
    return Male.MIN_BIRTH_INTERVAL;
  }

  public double getBirthChance() {
    return Human.getChanceByInterpolation(Male.AGE_BIRTH_CHANCES, this.getAge())
           + Human.getChanceByInterpolation(Male.HEALTH_BIRTH_CHANCES, this.getHealth());
  }
}