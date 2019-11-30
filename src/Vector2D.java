public class Vector2D {
  private double x;
  private double y;
  private double length;
  private double angle;

  private static final int LENGTH = 0b0001;
  private static final int ANGLE  = 0b0010;
  private static final int X      = 0b0100;
  private static final int Y      = 0b1000;
  private static final int POS    = 0b1100;

  public Vector2D(double x, double y) {
    this.x = x;
    this.y = y;
    this.update(Vector2D.LENGTH|Vector2D.ANGLE);
  }

  public Vector2D(double x, double y, double length) {
    this.x = x;
    this.y = y;
    this.length = length;
    this.update(Vector2D.ANGLE|Vector2D.X|Vector2D.Y);
  }

  @Override
  public String toString() {
    return "Vector2D "+this.x+" "+this.y+" "+Math.toDegrees(this.angle)+" "+this.length;
  }

  public double getLength() {
    return this.length;
  }

  public double getAngle() {
    return this.angle;
  }

  public Vector2D setPos(double x, double y) {
    this.x = x;
    this.y = y;
    this.update(Vector2D.LENGTH|Vector2D.ANGLE);
    return this;
  }

  public Vector2D setLength(double length) {
    this.length = length;
    this.update(Vector2D.POS);
    return this;
  }

  public Vector2D setAngle(double angle) {
    this.angle = angle%(2*Math.PI);
    this.update(Vector2D.POS);
    return this;
  }

  public Vector2D flip() {
    this.x = -this.x;
    this.y = -this.y;
    this.update(Vector2D.ANGLE);
    return this;
  }

  public int asMoveInteger() {
    if(this.getLength() == 0) {
      return Cheerville.NO_MOVE;
    } else {
      int angle = (int)(Math.toDegrees(this.getAngle()));
      if(angle >= -45 && angle < 45) {
        return Cheerville.EAST;
      } else if(angle >= 45 && angle < 180-45) {
        return Cheerville.SOUTH;
      } else if(angle >= -180+45 && angle < -45) {
        return Cheerville.NORTH;
      } else {
        return Cheerville.WEST;
      }
    }
  }

  public void update(int bitmask) {
    if((bitmask&Vector2D.LENGTH) > 0) {
      this.length = Math.sqrt((x*x)+(y*y));
    }
    if((bitmask&Vector2D.ANGLE) > 0) {
      this.angle = Math.atan2(this.y, this.x);
    }
    if((bitmask&Vector2D.X) > 0) {
      this.x = Math.cos(this.angle)*this.length;
    }
    if((bitmask&Vector2D.Y) > 0) {
      this.y = Math.sin(this.angle)*this.length;
    }
  }

  public void add(Vector2D other) {
    this.setPos(this.x+other.x, this.y+other.y);
  }
}