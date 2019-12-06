import java.awt.Color;

import java.util.TreeSet;
import java.util.Iterator;

public abstract class Spawnable implements Comparable<Spawnable> {
  private int x, y;
  private int health;

  private TreeSet<Spawnable> descendants = new TreeSet<Spawnable>();
  private final long id;

  public Spawnable(int x, int y, int initialHealth) {
    this.x = x;
    this.y = y;
    this.health = initialHealth;
    this.id = this.generateID();
  }

  @Override
  public int compareTo(Spawnable other) {
    return (int)(this.getID()-other.getID());
  }

  public void printDescendants() {
    System.out.println(this.descendants.toString());
  }

  public String[] getDescendantStrings() {
    this.removeDeadDescendants();
    if (this.descendants.isEmpty()) {
      return new String[0];
    }
    String[] s = new String[this.descendants.size()];
    Spawnable next = this.descendants.first();
    for (int i = 0; i < this.descendants.size(); ++i) {
      s[i] = next.toString();
      next = this.descendants.higher(next);
    }
    return s;
  }

  public Spawnable getFirstDescendant() {
    this.removeDeadDescendants();
    this.printDescendants();
    if (this.descendants.isEmpty()) {
      return null;
    } else {
      return this.descendants.first();
    }
  }

  public boolean hasDescendant(Spawnable descendant) {
    return this.descendants.contains(descendant);
  }

  public Spawnable addDescendant(Spawnable descendant) {
    this.descendants.add(descendant);
    this.removeDeadDescendants();
    return descendant;
  }

  private void removeDeadDescendants() {
    Iterator<Spawnable> descendentIterator = this.descendants.iterator();
    while (descendentIterator.hasNext()) {
      if (descendentIterator.next().getHealth() <= 0) {
        descendentIterator.remove();
      }
    }
  }

  public int getHealth() {
    return this.health;
  }

  public int heal(int amount) {
    if (amount > 0) {
      this.health += amount;
      this.health = Math.min(this.health, this.getMaxHealth());
    }
    return this.health;
  }

  public int decay() {
    --this.health;
    return this.health;
  }

  public void setDead() {
    this.health = 0;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public void setPos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getColorChannelValue() {
    return (this.getMaxHealth()-this.getHealth())*(255/this.getMaxHealth());
  }

  public long getID() {
    return this.id;
  }

  public abstract Spawnable act(Spawnable other);

  public abstract int getMaxHealth();

  public abstract int getInitialHealth();

  public abstract int getHealthVariance();

  public abstract Color getColor();

  public abstract long generateID();
}