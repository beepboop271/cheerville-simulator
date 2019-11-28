public class Zombie extends Moveable {
  private static final int INITIAL_HEALTH = 20;
  private static final int HEALTH_VARIANCE = 5;
  private static final int MAX_HEALTH = 30;

  private static final double HUMAN_ENERGY_FACTOR = 0.1;

  public Zombie(int x, int y) {
    super(x, y,
          (Zombie.INITIAL_HEALTH
           + (int)(((Math.random()-0.5)*2)  // [-1, 1) * (variance+1)
                   * (Zombie.HEALTH_VARIANCE+1))));
  }

  public Zombie(Human victim) {
    super(victim.getX(), victim.getY(),
          victim.getHealth());
    victim.setDead();
  }

  @Override
  public String toString() {
    return "Zombie#"+this.getID();
  }

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
      return this;
    }
    // otherwise allow the movement
    return null;
  }

  public Zombie attackHuman(Human victim) {
    // System.out.println("attack");
    if(this.getHealth() > victim.getHealth()) {
      this.heal((int)(victim.getHealth()*Zombie.HUMAN_ENERGY_FACTOR));
      victim.setDead();
      return null;
    } else {
      return (Zombie)victim.addDescendant(new Zombie(victim));
    }
  }

  public int getMaxHealth() {
    return Zombie.MAX_HEALTH;
  }

  public int getInitialHealth() {
    return Zombie.INITIAL_HEALTH;
  }

  public int getHealthVariance() {
    return Zombie.HEALTH_VARIANCE;
  }
}