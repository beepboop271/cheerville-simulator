public abstract class Moveable extends Spawnable {
  private boolean moved = false;

  public Moveable(int x, int y, int initialHealth) {
    super(x, y, initialHealth);
  }

  public boolean getMoved() {
    return this.moved;
  }

  public void setMoved(boolean moved) {
    this.moved = moved;
  }

  public int[] generateRandomMove() {
    int direction = (int)(Math.random()*5);
    int[] pos = new int[2];
    if (direction == 0) {
      pos[0] = this.getX()+1;
      pos[1] = this.getY();
    } else if (direction == 1) {
      pos[0] = this.getX()-1;
      pos[1] = this.getY();
    } else if (direction == 2) {
      pos[0] = this.getX();
      pos[1] = this.getY()+1;
    } else if (direction == 3) {
      pos[0] = this.getX();
      pos[1] = this.getY()-1;
    } else {
      pos[0] = this.getX();
      pos[1] = this.getY();
    }
    return pos;
  }
}