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
  }

  @Override
  public String toString() {
    return "Z";
  }

  public Spawnable act(Spawnable other) {
    if (other instanceof Plant) {
      // null = zombie can move to the spot
      // and nothing new spawns
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

  // public void act() {
  //   this.getContainingWorld().removeMapAt(this.getX(), this.getY());
  //   int lastX, lastY;
  //   lastX = this.getX();
  //   lastY = this.getY();
  //   int[] nextPos = this.generateRandomMove();
  //   if (this.getContainingWorld().isOccupiedAt(nextPos[0], nextPos[1])) {
  //     Spawnable collidedSpawnable = this.getContainingWorld()
  //                                       .getMapAt(nextPos[0], nextPos[1]);
  //     if (collidedSpawnable instanceof Plant) {
  //       // System.out.println("hit plant");
  //       this.moveTo(nextPos[0], nextPos[1]);
  //     } else if (collidedSpawnable instanceof Human) {
  //       // System.out.println("wtf");
  //       Zombie newZombie = this.attackHuman((Human)collidedSpawnable);
  //       if (newZombie != null) {
  //         newZombie.moveTo(nextPos[0], nextPos[1]);
  //         this.moveTo(lastX, lastY);
  //       } else {
  //         this.moveTo(nextPos[0], nextPos[1]);
  //       }
  //     } else if (collidedSpawnable instanceof Zombie) {
  //       // System.out.println("hit zombie");
  //       // System.out.printf("going from (%d, %d) back to (%d, %d)\n",
  //       //                   this.getX(), this.getY(),
  //       //                   lastX, lastY);
  //       this.moveTo(lastX, lastY);
  //     }
  //   } else {
  //     // System.out.println("hit nothing");
  //     this.moveTo(nextPos[0], nextPos[1]);
  //   }
  // }

  public Zombie attackHuman(Human victim) {
    // System.out.println("attack");
    if(this.getHealth() > victim.getHealth()) {
      this.heal((int)(victim.getHealth()*Zombie.HUMAN_ENERGY_FACTOR));
      return null;
    } else {
      return new Zombie(victim);
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