public abstract class Moveable extends Spawnable {
  private boolean moved = false;
  private int facingDirection;
  private Spawnable[][] vision;

  public Moveable(int x, int y, int initialHealth) {
    super(x, y, initialHealth);
    this.setRandomFacingDirection();
  }

  public boolean getMoved() {
    return this.moved;
  }

  public void setMoved(boolean moved) {
    this.moved = moved;
  }

  public int getFacingDirection() {
    return this.facingDirection;
  }

  public void setFacingDirection(int facingDirection) {
    this.facingDirection = facingDirection;
  }

  public void setRandomFacingDirection() {
    this.facingDirection = (int)(Math.random()*4)+1;
  }

  public void turnAround() {
    if(this.facingDirection == Cheerville.NORTH) {
      this.facingDirection = Cheerville.SOUTH;
    } else if(this.facingDirection == Cheerville.SOUTH) {
      this.facingDirection = Cheerville.NORTH;
    } else if(this.facingDirection == Cheerville.WEST) {
      this.facingDirection = Cheerville.EAST;
    } else if(this.facingDirection == Cheerville.EAST) {
      this.facingDirection = Cheerville.WEST;
    }
  }

  public Spawnable[][] getVision() {
    return this.vision;
  }

  public void setVision(Spawnable[][] vision) {
    this.vision = vision;
  }

  public Vector2D getDistanceVectorTo(Spawnable other) {
    // System.out.printf("at (%d, %d), other is at (%d, %d), so vec is %s\n",
    //                   this.getX(), this.getY(), other.getX(), other.getY(),
    //                   new Vector2D(other.getX()-this.getX(), other.getY()-this.getY()).toString());
    return new Vector2D(other.getX()-this.getX(), other.getY()-this.getY());
  }

  public int[] generateRandomMove() {
    int direction = (int)(Math.random()*5);
    int[] pos = new int[2];
    if (direction == 0) {
      pos[0] = this.getX()+1;
      pos[1] = this.getY();
      this.setFacingDirection(Cheerville.EAST);
    } else if (direction == 1) {
      pos[0] = this.getX()-1;
      pos[1] = this.getY();
      this.setFacingDirection(Cheerville.WEST);
    } else if (direction == 2) {
      pos[0] = this.getX();
      pos[1] = this.getY()+1;
      this.setFacingDirection(Cheerville.SOUTH);
    } else if (direction == 3) {
      pos[0] = this.getX();
      pos[1] = this.getY()-1;
      this.setFacingDirection(Cheerville.NORTH);
    } else {
      pos[0] = this.getX();
      pos[1] = this.getY();
    }
    return pos;
  }

  abstract int[] generateSmartMove();
}