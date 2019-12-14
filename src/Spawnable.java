import java.awt.Color;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * [Spawnable]
 * A being that can exist in the Cheerville World.
 * 2019-12-06
 * @version 1.5
 * @author Kevin Qiao
 */
public abstract class Spawnable implements Comparable<Spawnable> {
  private int x, y;
  private int health;

  private TreeSet<Spawnable> descendants = new TreeSet<Spawnable>();
  private final long id;

  
  /** 
   * [Spawnable]
   * Constructor for a Spawnable with the given position and health.
   * @param x             The x coordinate of the Spawnable.
   * @param y             The y coordinate of the Spawnable.
   * @param initialHealth The health of the Spawnable.
   */
  public Spawnable(int x, int y, int initialHealth) {
    this.x = x;
    this.y = y;
    this.health = initialHealth;
    this.id = this.generateID();
  }

  
  /** 
   * [compareTo]
   * Compares two Spawnables by their ID number.
   * @param other The Spawnable to be compared with
   * @return int, a negative integer if this Spawnable is
   *         less than the other, zero if they are equal, and
   *         a positive integer if this Spawnable is greater.
   */
  @Override
  public int compareTo(Spawnable other) {
    return (int)(this.getID()-other.getID());
  }

  
  /** 
   * [getDescendantStrings]
   * Gets an array of the descendants of this Spawnable in their
   * String representations.
   * @return String[], the String representations of this
   *         Spawnable's descendants.
   */
  public String[] getDescendantStrings() {
    this.removeDeadDescendants();
    if (this.descendants.isEmpty()) {
      return new String[0];
    }
    String[] s = new String[this.descendants.size()];
    Spawnable next = this.descendants.first();
    int i = 0;
    while(i < this.descendants.size()) {
      s[i] = next.toString();
      next = this.descendants.higher(next);
      ++i;
    }
    return s;
  }

  
  /** 
   * [getFirstDescendant]
   * Gets the descendant with the smallest ID from
   * this Spawnable, or null if there are none.
   * @return Spawnable, null if this Spawnable has no
   *         descendants, or the descendant with the smallest
   *         ID.
   */
  public Spawnable getFirstDescendant() {
    this.removeDeadDescendants();
    if (this.descendants.isEmpty()) {
      return null;
    } else {
      return this.descendants.first();
    }
  }

  
  /** 
   * [hasDescendant]
   * Returns whether or not the given Spawnable is a
   * descendant of this Spawnable.
   * @param descendant The potential descendant of this Spawnable
   *                   to check.
   * @return boolean, true if the given Spawnable is a descendant
   *         of this Spawnable, false otherwise.
   */
  public boolean hasDescendant(Spawnable descendant) {
    return this.descendants.contains(descendant);
  }

  
  /** 
   * [addDescendant]
   * Adds the given Spawnable to this Spawnable's descendants.
   * @param descendant The Spawnable to add.
   * @return Spawnable, the Spawnable added.
   */
  public Spawnable addDescendant(Spawnable descendant) {
    this.descendants.add(descendant);
    this.removeDeadDescendants();
    return descendant;
  }


  /**
   * [removeDeadDescendants]
   * Removes any dead descendants from this Spawnable's records.
   */
  private void removeDeadDescendants() {
    Iterator<Spawnable> descendentIterator = this.descendants.iterator();
    while (descendentIterator.hasNext()) {
      if (descendentIterator.next().getHealth() <= 0) {
        descendentIterator.remove();
      }
    }
  }

  
  /** 
   * [getHealth]
   * Returns the health of this Spawnable.
   * @return int, the health of this Spawnable.
   */
  public int getHealth() {
    return this.health;
  }

  
  /** 
   * [heal]
   * Adds the given amount of health to this Spawnable,
   * up to the maximum health of this Spawnable.
   * @param amount The amount of health to add.
   * @return int, the health of this Spawnable after healing.
   */
  public int heal(int amount) {
    if (amount > 0) {
      this.health += amount;
      this.health = Math.min(this.health, this.getMaxHealth());
    }
    return this.health;
  }

  
  /** 
   * [decay]
   * Reduces and returns this Spawnable's health.
   * @return int, the health of this Spawnable after decaying.
   */
  public int decay() {
    // limit it to maxHealth since the user could
    // change maxHealth to be lower than the health of
    // existing plants and we want that change to occur
    this.health = Math.min(this.health-1, this.getMaxHealth());
    return this.health;
  }


  /**
   * [setDead]
   * Sets this Spawnable as dead, no health.
   */
  public void setDead() {
    this.health = 0;
  }

  
  /** 
   * [getX]
   * Returns the x coordinate of this Spawnable.
   * @return int, the x coordinate of this Spawnable.
   */
  public int getX() {
    return this.x;
  }

  
  /** 
   * [getY]
   * Returns the y coordinate of this Spawnable.
   * @return int, the y coordinate of this Spawnable.
   */
  public int getY() {
    return this.y;
  }

  
  /** 
   * [setPos]
   * Sets the position of this Spawnable.
   * @param x The new x coordinate of this Spawnable.
   * @param y The new y coordinate of this Spawnable.
   */
  public void setPos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  
  /** 
   * [getColorChannelValue]
   * Calculates the correct value of any non-255 channels
   * in the color of this Spawnable based on health. E.g: Plants
   * always have 255 for green, then use the value returned from
   * this method for red and blue to slowly fade to white as
   * they lose health.
   * @return int, the value of any variable color channels.
   */
  public int getColorChannelValue() { 
    return Math.min(255,
                    (Math.max(0, this.getMaxHealth()-this.getHealth())
                     * (255/this.getMaxHealth())));
  }

  
  /** 
   * [getID]
   * Returns the ID of this Spawnable.
   * @return long, the ID of this Spawnable.
   */
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