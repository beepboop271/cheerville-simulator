public abstract class Spawnable {
  private int x, y;
  private int health;
  static int ID = 0;
  final int id;

  public Spawnable(int x, int y, int initialHealth) {
    this.x = x;
    this.y = y;
    this.health = initialHealth;
    this.id = ID++;
  }

  public int getID() {
    return this.id;
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
    return this.health;
  }

  abstract Spawnable act(Spawnable other);

  abstract int getMaxHealth();

  abstract int getInitialHealth();

  abstract int getHealthVariance();
}