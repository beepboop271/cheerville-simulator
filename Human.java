public abstract class Human extends Moveable {
  private static final int INITIAL_HEALTH = 20;
  private static final int HEALTH_VARIANCE = 5;
  private static final int MAX_HEALTH = 30;

  private static final double FEMALE_CHANCE = 0.5;

  private static final double PLANT_ENERGY_FACTOR = 0.5;

  private int age = 0;
  private int stepsUntilFertile = 0;

  public Human(int x, int y) {
    super(x, y,    
          (Human.INITIAL_HEALTH
           + (int)(((Math.random()-0.5)*2)  // [-1, 1) * (variance+1)
                   * (Human.HEALTH_VARIANCE+1))));
  }

  public Human(int x, int y, int initialHealth) {
    super(x, y, initialHealth);
  }

  public static Human createHuman(int x, int y) {
    if(Math.random() < Human.FEMALE_CHANCE) {
      return new Female(x, y);
    } else {
      return new Male(x, y);
    }
  }

  public static Human createHuman(int x, int y, int initialHealth) {
    if(Math.random() < Human.FEMALE_CHANCE) {
      return new Female(x, y, initialHealth);
    } else {
      return new Male(x, y, initialHealth);
    }
  }

  @Override
  public String toString() {
    // return "Human#"+this.getID();
    return "H";
  }

  public Spawnable act(Spawnable other) {
    if (other instanceof Plant) {
      // System.out.println("plant");
      this.heal((int)(other.getHealth()*Human.PLANT_ENERGY_FACTOR));
      other.setDead();
      other.addDescendant(this);
      return null;
    } else if (other instanceof Human) {
      // System.out.println("human");
      Human newHuman = this.tryReproduceWith((Human)other);
      if (newHuman == null) {
        return this;
      } else {
        return this.addDescendant(newHuman);
      }
    } else if (other instanceof Zombie) {
      // System.out.println("zombie");
      Zombie newZombie = ((Zombie)other).attackHuman(this);
      if (newZombie == null) {
        this.setDead();
        return this;
      } else {
        return newZombie;
      }
    }
    return null;
  }

  public int getMaxHealth() {
    return Human.MAX_HEALTH;
  }

  public int getInitialHealth() {
    return Human.INITIAL_HEALTH;
  }

  public int getHealthVariance() {
    return Human.HEALTH_VARIANCE;
  }

  public int getAge() {
    return this.age;
  }

  public void incrementAge() {
    ++this.age;
  }

  public int getStepsUntilFertile() {
    return this.stepsUntilFertile;
  }

  public void setStepsUntilFertile(int stepsUntilFertile) {
    this.stepsUntilFertile = Math.max(0, stepsUntilFertile);
  }

  public boolean canReproduce() {
    return (this.getStepsUntilFertile() == 0)
           && (Math.random() < this.getBirthChance());
  }

  public Human tryReproduceWith(Human other) {
    if(this.canReproduce() && other.canReproduce()
          && (((this instanceof Male) && (other instanceof Female))
              || ((this instanceof Female) && (other instanceof Male)))) {
      this.setStepsUntilFertile(this.getMinBirthInterval());
      return Human.createHuman(this.getX(), this.getY(),
                               (this.getHealth()+other.getHealth())/2);
    }
    return null;
  }

  public static double linearInterpolate(double start,
                                         double end,
                                         double steps,
                                         double stepStart,
                                         double stepEnd) {
    return start+((end-start)*((steps-stepStart)/(stepEnd-stepStart)));
  }

  public static double getChanceByInterpolation(double[][] chances, int value) {
    for(int i = 0; i < chances.length; ++i) {
      if(chances[i][0] > value) {
        return Human.linearInterpolate(chances[i-1][1],
                                       chances[i][1],
                                       value,
                                       chances[i-1][0],
                                       chances[i][0]);
      }
    }
    return chances[chances.length-1][1];
  }

  abstract int getMinBirthInterval();

  abstract double getBirthChance();
}