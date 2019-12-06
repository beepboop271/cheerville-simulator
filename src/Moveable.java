public abstract class Moveable extends Spawnable {
  private boolean moved = false;
  private int facingDirection;
  private Spawnable[][] vision;
  private Vector2D[][] influences;

  
  /** 
   * [Moveable]
   * Constructor for a Moveable with the given position and health.
   * @param x             The x coordinate of the Moveable.
   * @param y             The y coordinate of the Moveable.
   * @param initialHealth The health of the Moveable.
   */
  public Moveable(int x, int y, int initialHealth) {
    super(x, y, initialHealth);
    this.setRandomFacingDirection();
  }


  /**
   * [turnAround]
   * Turns around 180 degrees to face the opposite direction.
   */
  public void turnAround() {
    if (this.facingDirection == Cheerville.NORTH) {
      this.facingDirection = Cheerville.SOUTH;
    } else if (this.facingDirection == Cheerville.SOUTH) {
      this.facingDirection = Cheerville.NORTH;
    } else if (this.facingDirection == Cheerville.WEST) {
      this.facingDirection = Cheerville.EAST;
    } else if (this.facingDirection == Cheerville.EAST) {
      this.facingDirection = Cheerville.WEST;
    }
  }

  
  /** 
   * [generateRandomMove]
   * Returns random [x, y] of a new location that this Moveable
   * can move to.
   * @return int[], the [x, y] array of the new location.
   */
  public int[] generateRandomMove() {
    int move = (int)(Math.random()*5);
    if (move != Cheerville.NO_MOVE) {
      this.setFacingDirection(move);
    }
    return this.moveIntToPos(move);
  }

  
  /** 
   * [generateSmartMove]
   * Returns [x, y] of a new location that this Moveable can
   * move to. The new location is the best direction to move in
   * based on the vision of the Moveable.
   * @return int[], the [x, y] array of the new location.
   */
  public int[] generateSmartMove() {
    if (Math.random() < this.getRandomMoveChance()) {
      return this.generateRandomMove();
    }
    if (this.getVisionValue() == 0) {
      this.influences = null;
      return this.generateRandomMove();
    }
    Vector2D direction = new Vector2D(0, 0);
    Spawnable[][] vision = this.getVision();
    this.influences = new Vector2D[vision.length][vision[0].length];

    Vector2D influence;
    Spawnable s;
    for (int i = 0; i < vision.length; ++i) {
      for (int j = 0; j < vision[0].length; ++j) {
        s = vision[i][j];
        if (s != null) {
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
    if (move == 0) {
      return this.generateRandomMove();
    } else {
      this.setFacingDirection(move);
      return this.moveIntToPos(move);
    }
  }

  
  /** 
   * [moveIntToPos]
   * Converts a direction constant (e.g. Cheerville.NORTH)
   * into the location this Moveable would be in if it
   * moved in that direction.
   * @param moveInt The direction constant from Cheerville.java
   * @return int[], the [x, y] array of the location.
   */
  public int[] moveIntToPos(int moveInt) {
    int[] pos = {this.getX()+Cheerville.MOVEMENTS[moveInt][0],
                 this.getY()+Cheerville.MOVEMENTS[moveInt][1]};
    return pos;
  }

  
  /** 
   * [getMoved]
   * Returns whether or not this Moveable has acted in
   * the current time step.
   * @return boolean, whether or not this Moveable has acted.
   */
  public boolean getMoved() {
    return this.moved;
  }

  
  /** 
   * [setMoved]
   * Set whether or not this Moveable has acted in the
   * current time step.
   * @param moved
   */
  public void setMoved(boolean moved) {
    this.moved = moved;
  }

  
  /** 
   * [getFacingDirection]
   * Get the direction constant (e.g. Cheerville.NORTH)
   * that this Moveable last moved in, the direction
   * it is facing.
   * @return int, the direction constant that this Moveable
   *         is facing.
   */
  public int getFacingDirection() {
    return this.facingDirection;
  }

  
  /** 
   * [setFacingDirection]
   * Set the direction that this Moveable is facing.
   * @param facingDirection The direction constant this
   *                        Moveable is facing in.
   */
  public void setFacingDirection(int facingDirection) {
    this.facingDirection = facingDirection;
  }


  /**
   * [setRandomFacingDirection]
   * Randomly sets the direction that this Moveable is
   * facing in to one of the four cardinal directions.
   */
  public void setRandomFacingDirection() {
    this.facingDirection = (int)(Math.random()*4)+1;
  }

  
  /** 
   * [getVision]
   * Returns the Spawnables that this Moveable can see.
   * @return Spawnable[][], the part of the World visible
   *         to the Moveable.
   */
  public Spawnable[][] getVision() {
    return this.vision;
  }

  
  /** 
   * [setVision]
   * Sets what Spawnables are visible to this Moveable.
   * @param vision The part of the World visible to
   *               the Moveable.
   */
  public void setVision(Spawnable[][] vision) {
    this.vision = vision;
  }

  
  /** 
   * [getInfluences]
   * Returns the influence vectors for each Spawnable
   * visible to this Moveable.
   * @return Vector2D[][], the influences for each Spawnable
   *         this Moveable can see.
   */
  public Vector2D[][] getInfluences() {
    return this.influences;
  }

  
  /** 
   * [getDistanceVectorTo]
   * Creates a new Vector2D which represents the distance
   * from this Moveable to the other.
   * @param other The Moveable to get distance to.
   * @return Vector2D, the distance vector from this Moveable
   *         to the other.
   */
  public Vector2D getDistanceVectorTo(Spawnable other) {
    return new Vector2D(other.getX()-this.getX(), other.getY()-this.getY(), other.getColor());
  }

  public abstract Vector2D getInfluenceVectorFor(Spawnable other);

  public abstract int getVisionValue();

  public abstract double getRandomMoveChance();
}