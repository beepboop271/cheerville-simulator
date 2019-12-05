import java.awt.Color;

import java.util.TreeSet;
import java.util.Iterator;

public abstract class Spawnable implements Comparable<Spawnable> {
  private int x, y;
  private int health;

  // for display
  private TreeSet<Spawnable> descendants = new TreeSet<Spawnable>();
  private final long id;

  public Spawnable(int x, int y, int initialHealth) {
    this.x = x;
    this.y = y;
    this.health = initialHealth;
    this.id = this.generateID();
  }

  public abstract long generateID();

  public long getID() {
    return this.id;
  }

  public int compareTo(Spawnable other) {
    return (int)(this.getID()-other.getID());
  }

  public void printDescendants() {
    // this.removeDeadDescendants();
    System.out.println(this.descendants.toString());
  }

  public String[] getDescendantStrings() {
    this.removeDeadDescendants();
    String[] s = new String[this.descendants.size()];
    Iterator<Spawnable> descendentIterator = this.descendants.iterator();
    int i = 0;
    while(descendentIterator.hasNext()) {
      s[i++] = descendentIterator.next().toString();
    }
    return s;
  }

  public Spawnable getFirstDescendant() {
    this.removeDeadDescendants();
    this.printDescendants();
    if(this.descendants.isEmpty()) {
      return null;
    } else {
      return this.descendants.first();
    }
  }

  public Spawnable addDescendant(Spawnable descendant) {
    if(!this.descendants.contains(descendant)) {
      this.descendants.add(descendant);
    }
    this.removeDeadDescendants();
    return descendant;
  }

  private void removeDeadDescendants() {
    // this.printDescendants();
    Iterator<Spawnable> descendentIterator = this.descendants.iterator();
    while(descendentIterator.hasNext()) {
      if(descendentIterator.next().getHealth() <= 0) {
        // System.out.println("kms");
        descendentIterator.remove();
      }
    }
    // this.printDescendants();
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

  public abstract Spawnable act(Spawnable other);

  public abstract int getMaxHealth();

  public abstract int getInitialHealth();

  public abstract int getHealthVariance();

  public abstract Color getColor();

  public int getColorChannelValue() {
    return (this.getMaxHealth()-this.getHealth())*(255/this.getMaxHealth());
  }
}