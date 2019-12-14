public class Cheerville {
  public static final int NO_MOVE = 0;
  public static final int NORTH = 1;
  public static final int EAST = 2;
  public static final int SOUTH = 3;
  public static final int WEST = 4;
  public static final int[][] MOVEMENTS = {
    {0, 0},
    {0, -1},
    {1, 0},
    {0, 1},
    {-1, 0}
  };

  public static void main(String[] args) throws InterruptedException {
    new CheervilleManager().run();
  }
}