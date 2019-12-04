public abstract class Moveable extends Spawnable {
  private boolean moved = false;
  private int facingDirection;
  private Spawnable[][] vision;
  private Vector2D[][] influences;  // for display

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

  public Vector2D[][] getInfluences() {
    return this.influences;
  }

  public Vector2D getDistanceVectorTo(Spawnable other) {
    return new Vector2D(other.getX()-this.getX(), other.getY()-this.getY(), other.getColor());
  }

  public int[] generateRandomMove() {
    int move = (int)(Math.random()*5);
    // int[] pos = new int[2];
    if(move != Cheerville.NO_MOVE) {
      this.setFacingDirection(move);
    }
    return this.moveIntToPos(move);
  }

  public int[] generateSmartMove() {
    if(Math.random() < this.getRandomMoveChance()) {
      return this.generateRandomMove();
    }
    if(this.getVisionValue() == 0) {
      this.influences = null;
      return this.generateRandomMove();
    }
    Vector2D direction = new Vector2D(0, 0);
    Spawnable[][] vision = this.getVision();
    this.influences = new Vector2D[vision.length][vision[0].length];

    Vector2D influence;
    Spawnable s;
    for(int i = 0; i < vision.length; ++i) {
      for(int j = 0; j < vision[0].length; ++j) {
        s = vision[i][j];
        if(s != null) {
          if (s != this) {
            influence = this.getInfluenceVectorFor(s);
            this.influences[i][j] = influence;
            direction.add(influence);
          } else {
            // store the decision vector in influences as well
            // so that we can draw it
            this.influences[i][j] = direction;
          }
        }
      }
    }

    int move = direction.asMoveInteger();
    if(move == 0) {
      return this.generateRandomMove();
    } else {
      this.setFacingDirection(move);
      return this.moveIntToPos(move);
    }
  }

  public int[] moveIntToPos(int moveInt) {
    int[] pos = {this.getX()+Cheerville.MOVEMENTS[moveInt][0],
                 this.getY()+Cheerville.MOVEMENTS[moveInt][1]};
    return pos;
  }

  abstract Vector2D getInfluenceVectorFor(Spawnable other);

  abstract int getVisionValue();

  abstract double getRandomMoveChance();
}