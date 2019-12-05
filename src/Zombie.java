import java.awt.Color;

public class Zombie extends Moveable {
  private static final int INITIAL_HEALTH = 10;
  private static final int HEALTH_VARIANCE = 5;
  private static final int MAX_HEALTH = 20;

  private static final double HUMAN_ENERGY_FACTOR = 0.1;

  private static final double RANDOM_MOVE_CHANCE = 0.8;

  private static long NUM_ZOMBIES = 0;

  public Zombie(int x, int y) {
    super(x, y,
          (Zombie.INITIAL_HEALTH
           + (int)(((Math.random()-0.5)*2)  // [-1, 1) * (variance+1)
                   * (Zombie.HEALTH_VARIANCE+1))));
  }

  public Zombie(Human victim) {
    super(victim.getX(), victim.getY(),
          Math.min(Zombie.MAX_HEALTH, victim.getHealth()));
    victim.setDead();
  }

  @Override
  public String toString() {
    return "Zombie#"+this.getID();
    // return "Z";
  }

  public long generateID() {
    return Zombie.NUM_ZOMBIES++;
  }

  public int getVisionValue() {
    Spawnable[][] vision = this.getVision();
    int value = 0;
    if(vision != null) {
      for(int i = 0; i < vision.length; ++i) {
        for(int j = 0; j < vision[0].length; ++j) {
          if(vision[i][j] instanceof Human) {
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
    if(other instanceof Human) {
      // ++humansSpotted;
      // inversely proportional to zombie's health
      // proportional to the human's health
      // inversely proportional to human's distance
      influence.setLength((10.0/this.getHealth())
                          * other.getHealth()
                          * (5.0/influence.getLength()));
    } else if (other instanceof Zombie) {
      // inversely proportional to zombie's distance
      influence.setLength(3.0/influence.getLength());
      // move away, not towards
      influence.flip();
    } else {
      influence.setLength(0);
    }
    return influence;
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
      this.turnAround();
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

  public double getRandomMoveChance() {
    return Zombie.RANDOM_MOVE_CHANCE;
  }

  public Color getColor() {
    return new Color(255, this.getColorChannelValue(), this.getColorChannelValue());
  }
}