import java.awt.Color;
/**
 * [Vector2D]
 * A 2D vector specified with rectangular or polar coordinates.
 * 2019-12-06
 * @version 1.2
 * @author Kevin Qiao
 */
public class Vector2D implements Cloneable {
  private double x;
  private double y;
  private double length;
  private double angle;
  private Color vectorColor;

  private static final int LENGTH = 0b0001;
  private static final int ANGLE  = 0b0010;
  private static final int X      = 0b0100;
  private static final int Y      = 0b1000;
  private static final int POS    = 0b1100;

  
  /** 
   * [Vector2D]
   * Constructor for a Vector2D with the given position.
   * @param x The x component of the Vector2D.
   * @param y The y component of the Vector2D.
   */
  public Vector2D(double x, double y) {
    this.x = x;
    this.y = y;
    this.update(Vector2D.LENGTH|Vector2D.ANGLE);
    this.vectorColor = new Color(255, 255, 255);
  }

  /** 
   * [Vector2D]
   * Constructor for a Vector2D with the given position and color.
   * @param x           The x component of the Vector2D.
   * @param y           The y component of the Vector2D.
   * @param vectorColor The display color of the Vector2D.
   */
  public Vector2D(double x, double y, Color vectorColor) {
    this(x, y);
    this.vectorColor = vectorColor;
  }

  
  /** 
   * [clone]
   * Creates a shallow copy of the Vector2D.
   * @return Object, the copied Vector2D, or null if the copy failed.
   */
  @Override
  public Object clone() {
    try {
      return super.clone();  
    } catch (CloneNotSupportedException e){ 
      return null; 
    }
  }

  
  /** 
   * [asMoveInteger]
   * Returns the cardinal direction closest to this
   * Vector's direction.
   * @return int, the direction constant closest to this
   *         Vector's direction.
   */
  public int asMoveInteger() {
    if (this.getLength() == 0) {
      return Cheerville.NO_MOVE;
    } else {
      int angle = (int)(Math.toDegrees(this.getAngle()));
      if ((angle >= -45) && (angle < 45)) {
        return Cheerville.EAST;
      } else if ((angle >= 45) && (angle < 180-45)) {
        return Cheerville.SOUTH;
      } else if ((angle >= -180+45) && (angle < -45)) {
        return Cheerville.NORTH;
      } else {
        return Cheerville.WEST;
      }
    }
  }

  
  /** 
   * [update]
   * Recalculates the specified properties, given by
   * bitwise combination of flag constants.
   * @param flags The bit flags which indicate what to recalculate.
   */
  private void update(int flags) {
    if ((flags&Vector2D.LENGTH) > 0) {
      this.length = Math.sqrt((x*x)+(y*y));
    }
    if ((flags&Vector2D.ANGLE) > 0) {
      this.angle = Math.atan2(this.y, this.x);
    }
    if ((flags&Vector2D.X) > 0) {
      this.x = Math.cos(this.angle)*this.length;
    }
    if ((flags&Vector2D.Y) > 0) {
      this.y = Math.sin(this.angle)*this.length;
    }
  }
  
  
  /** 
   * [flip]
   * Negates this Vector2D.
   */
  public void flip() {
    this.x = -this.x;
    this.y = -this.y;
    this.update(Vector2D.ANGLE);
  }

  
  /** 
   * [add]
   * Adds the given Vector2D to this Vector2D.
   * @param other The Vector2D to add.
   */
  public void add(Vector2D other) {
    this.setPos(this.x+other.x, this.y+other.y);
  }

  
  /** 
   * [getColor]
   * Returns the color of this Vector2D.
   * @return Color, the color of this Vector2D.
   */
  public Color getColor() {
    return this.vectorColor;
  }

  
  /** 
   * [getX]
   * Returns the x component of this Vector2D.
   * @return double, the x component of this Vector2D.
   */
  public double getX() {
    return this.x;
  }

  
  /** 
   * [getY]
   * Returns the y component of this Vector2D.
   * @return double, the y component of this Vector2D.
   */
  public double getY() {
    return this.y;
  }

  
  /** 
   * [setPos]
   * Sets the x and y components of this Vector2D.
   * @param x The new x component of this Vector2D.
   * @param y The new y component of this Vector2D.
   */
  public void setPos(double x, double y) {
    this.x = x;
    this.y = y;
    this.update(Vector2D.LENGTH|Vector2D.ANGLE);
  }

  
  /** 
   * [getLength]
   * Returns the length of this Vector2D.
   * @return double, the length of this Vector2D.
   */
  public double getLength() {
    return this.length;
  }

  
  /** 
   * [setLength]
   * Scales this Vector2D to the given length.
   * @param length The new length of this Vector2D.
   */
  public void setLength(double length) {
    if (length < 0) {
      this.length = Math.abs(length);
      this.update(Vector2D.POS);
      this.flip();
    } else {
      this.length = length;
      this.update(Vector2D.POS);
    }
  }

  
  /** 
   * [getAngle]
   * Returns the angle in radians of this Vector2D.
   * @return double, the angle in radians of this Vector2D.
   */
  public double getAngle() {
    return this.angle;
  }
}