import java.util.LinkedList;
import java.util.Iterator;

public abstract class Spawnable {
  private int x, y;
  private int health;

  // for display
  private LinkedList<Spawnable> descendants = new LinkedList<Spawnable>();
  private static long ID = 0;
  private final long id;

  public Spawnable(int x, int y, int initialHealth) {
    this.x = x;
    this.y = y;
    this.health = initialHealth;
    this.id = ID++;
  }

  public long getID() {
    return this.id;
  }

  public void printDescendants() {
    System.out.println(this.descendants.toString());
  }

  public String[] getDescendantStrings() {
    String[] s = new String[this.descendants.size()];
    Iterator<Spawnable> descendentIterator = this.descendants.iterator();
    int i = 0;
    while(descendentIterator.hasNext()) {
      s[i++] = descendentIterator.next().toString();
    }
    return s;
  }

  public Spawnable getFirstDescendant() {
    if(this.descendants.isEmpty()) {
      return null;
    } else {
      return this.descendants.getFirst();
    }
  }

  public Spawnable addDescendant(Spawnable descendant) {
    this.descendants.addLast(descendant);
    this.removeDeadDescendants();
    return descendant;
  }

  public void removeDeadDescendants() {
    Iterator<Spawnable> descendentIterator = this.descendants.iterator();
    while(descendentIterator.hasNext()) {
      if(descendentIterator.next().getHealth() <= 0) {
        descendentIterator.remove();
      }
    }
  }

  public int decay() {
    --this.health;
    return this.health;
  }

  public void setDead() {
    this.health = 0;
  }

  public int heal(int amount) {
    if (amount > 0) {
      this.health += amount;
      this.health = Math.min(this.health, this.getMaxHealth());
    }
    return this.health;
  }

  public void setPos(int x, int y) {
    // if(this instanceof Moveable) {
    //   System.out.printf("set to %d, %d\n", x, y);
    // }
    
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getHealth() {
    if(this.health < 0) {
      System.out.println("whaaaaaaaaaaat");
      return 0;
    }
    return this.health;
  }

  abstract Spawnable act(Spawnable other);

  abstract int getMaxHealth();

  abstract int getInitialHealth();

  abstract int getHealthVariance();
}