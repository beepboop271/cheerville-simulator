public abstract class Human extends Moveable {
  private static int initialHealth = 20;
  private static int healthVariance = 5;
  private static int maxHealth = 30;

  private static double femaleChance = 0.5;
  private static double plantEnergyFactor = 0.5;
  private static double randomMoveChance = 0.1;

  private static long numHumans = 0;

  private int age = 0;
  private int stepsUntilFertile = 0;

  
  /** 
   * [Human]
   * Constructor for a Human with the given position.
   * @param x The x coordinate of the Human.
   * @param y The y coordinate of the Human.
   */
  public Human(int x, int y) {
    super(x, y,    
          (Human.initialHealth
           + (int)(((Math.random()-0.5)*2)
                   * (Human.healthVariance+1))));
  }

  /** 
   * [Human]
   * Constructor for a Human with the given position and health.
   * @param x             The x coordinate of the Human.
   * @param y             The y coordinate of the Human.
   * @param initialHealth The health of the Human.
   */
  public Human(int x, int y, int initialHealth) {
    super(x, y, initialHealth);
  }

  
  /** 
   * [createHuman]
   * Randomly generates a Male or Female with the given position.
   * @param x The x coordinate of the Human.
   * @param y The y coordinate of the Human.
   * @return Human, the new Male or Female created.
   */
  public static Human createHuman(int x, int y) {
    if (Math.random() < Human.femaleChance) {
      return new Female(x, y);
    } else {
      return new Male(x, y);
    }
  }
  
  /** 
   * [createHuman]
   * Randomly generates a Male or Female with the given position
   * and health.
   * @param x             The x coordinate of the Human.
   * @param y             The y coordinate of the Human.
   * @param initialHealth The health of the Human.
   * @return Human, the new Male or Female created.
   */
  public static Human createHuman(int x, int y, int initialHealth) {
    if (Math.random() < Human.femaleChance) {
      return new Female(x, y, initialHealth);
    } else {
      return new Male(x, y, initialHealth);
    }
  }

  
  /** 
   * [linearInterpolate]
   * Linearly maps the range [stepStart, stepEnd] onto the range
   * [start, end], and returns the mapped value of steps.
   * @param start     The start value of the range to map to.
   * @param end       The end value of the range to map to.
   * @param steps     The value to map.
   * @param stepStart The start value of the range to map from.
   * @param stepEnd   The end value of the range to map from.
   * @return double, the mapped value of steps.
   */
  public static double linearInterpolate(double start,
                                         double end,
                                         double steps,
                                         double stepStart,
                                         double stepEnd) {
    return start+((end-start)*((steps-stepStart)/(stepEnd-stepStart)));
  }

  
  /** 
   * [getChanceByInterpolation]
   * Given an array of [value, chance] pairs such as age and 
   * reproduction chance at that given age, calculates the correct
   * chance for the given value by linearly interpolating between
   * the chances given. If the value goes past the last record in
   * the chance array, use the last chance.
   * @param chances The array of [value, chance] pairs to calculate with.
   * @param value   The value to get a chance for.
   * @return double, the correct chance for the given value.
   */
  public static double getChanceByInterpolation(double[][] chances, int value) {
    for (int i = 0; i < chances.length; ++i) {
      if (chances[i][0] > value) {
        return Human.linearInterpolate(chances[i-1][1],
                                       chances[i][1],
                                       value,
                                       chances[i-1][0],
                                       chances[i][0]);
      }
    }
    return chances[chances.length-1][1];
  }

  
  /** 
   * [toString]
   * Generates a String representation of the Human
   * using its ID.
   * @return String, the representation of this Human.
   */
  @Override
  public String toString() {
    return "Human#"+this.getID();
  }

  
  /** 
   * [act]
   * Determine how to act when colliding with the given
   * Spawnable.
   * @param other The Spawnable that this collided with.
   * @return Spawnable, either null to indicate this Human can
   *         move onto the location of the Spawnable, this to
   *         indicate the Human must move back to its original
   *         location, or a new Spawnable to indicate the Human
   *         must move back to its original location and create
   *         a new Spawnable on the World it is in.
   */
  @Override
  public Spawnable act(Spawnable other) {
    if (other instanceof Plant) {
      this.heal((int)(other.getHealth()*Human.plantEnergyFactor));
      other.setDead();
      other.addDescendant(this);
      return null;
    } else if (other instanceof Human) {
      Human newHuman = this.tryReproduceWith((Human)other);
      if (newHuman == null) {
        this.turnAround();
        return this;
      } else {
        ((Human)other).addDescendant(newHuman);
        return this.addDescendant(newHuman);
      }
    } else if (other instanceof Zombie) {
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

  
  /** 
   * [tryReproduceWith]
   * Attempts reproduction with another Human, returning null
   * or a new Human. Both Humans must be able to reproduce, be
   * of the opposite sex, and one cannot be a child of the other.
   * @param other The Human to attempt reproduction with.
   * @return Human, either null to indicate no reproduction occurred,
   *         or a new Human that is created.
   */
  public Human tryReproduceWith(Human other) {
    if (this.canReproduce()
          && other.canReproduce()
          && (((this instanceof Male) && (other instanceof Female))
              || ((this instanceof Female) && (other instanceof Male)))
          && !(this.hasDescendant(other) || other.hasDescendant(this))) {
      this.setStepsUntilFertile(this.getMinBirthInterval());
      return Human.createHuman(this.getX(), this.getY(),
                               (this.getHealth()+other.getHealth())/2);
    }
    return null;
  }

  
  /** 
   * [getBirthChance]
   * Determines the chance this Human can give birth. If the
   * Human recently gave birth, the chance is 0. Otherwise, the
   * chance is a combination of the Human's age and health.
   * @return double, the chance [0, 1] that this Human can
   *         reproduce.
   */
  public double getBirthChance() {
    if (this.getStepsUntilFertile() > 0) {
      return 0;
    }
    return Math.min(Math.max((Human.getChanceByInterpolation(this.getAgeBirthChances(),
                                                             this.getAge())
                              + Human.getChanceByInterpolation(this.getHealthBirthChances(),
                                                               this.getHealth())),
                             0.0),
                    1.0);
  }

  
  /** 
   * [generateID]
   * Generates a unique ID for this Human. A Human with
   * the ID of n is the nth Human to be created.
   * @return long
   */
  @Override
  public long generateID() {
    return Human.numHumans++;
  }

  
  /** 
   * [getMaxHealth]
   * Returns the maximum health a Human can have.
   * @return int, the maximum health possible for a Human.
   */
  @Override
  public int getMaxHealth() {
    return Human.maxHealth;
  }

  
  /** 
   * [getInitialHealth]
   * Returns the health a new Human with unspecified
   * health should have.
   * @return int, the default starting health of a Human.
   */
  @Override
  public int getInitialHealth() {
    return Human.initialHealth;
  }

  /** 
   * [getHealthVariance]
   * Returns the maximum difference between the
   * actual initial health of a Human and the
   * default initial health from getInitialHealth()
   * @return int, the maximum variance in initial health
   *         for a Human.
   */
  @Override
  public int getHealthVariance() {
    return Human.healthVariance;
  }

  
  /** 
   * [getRandomMoveChance]
   * Returns the chance [0, 1] a Human will move in a
   * randomly chose direction instead of determining the
   * best movement.
   * @return double, the chance [0, 1] a Human will move
   *         randomly.
   */
  @Override
  public double getRandomMoveChance() {
    return Human.randomMoveChance;
  }

  
  /** 
   * [getAge]
   * Returns the age of this Human.
   * @return int, the age.
   */
  public int getAge() {
    return this.age;
  }


  /**
   * [incrementAge]
   * Increases the age of this Human by one.
   */
  public void incrementAge() {
    ++this.age;
  }

  
  /** 
   * [getStepsUntilFertile]
   * Returns the amount of steps of time needed to pass
   * before this Human can reproduce again.
   * @return int, the amount of time steps before this Human
   *         can reproduce.
   */
  public int getStepsUntilFertile() {
    return this.stepsUntilFertile;
  }

  
  /** 
   * [setStepsUntilFertile]
   * Sets the amount of time steps before this Human can
   * reproduce again. Cannot be below 0.
   * @param stepsUntilFertile The amount of time steps before this
   *                          Human can reproduce.
   */
  public void setStepsUntilFertile(int stepsUntilFertile) {
    this.stepsUntilFertile = Math.max(0, stepsUntilFertile);
  }

  
  /** 
   * [canReproduce]
   * Determines if this Human (without considering another
   * Human) is able to reproduce. Must have been enough time since
   * any last reproduction and must either be very lucky or have
   * good health and age.
   * @return boolean, whether or not this Human is physically
   *         capable of reproducing.
   */
  public boolean canReproduce() {
    return (this.getStepsUntilFertile() == 0)
           && (Math.random() < this.getBirthChance());
  }

  public abstract int getMinBirthInterval();

  public abstract double[][] getAgeBirthChances();

  public abstract double[][] getHealthBirthChances();
}