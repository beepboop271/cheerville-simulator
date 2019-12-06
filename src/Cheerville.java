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
    World cheervilleWorld = new World(35, 35, 400, 0);
    CheervilleFrame display = new CheervilleFrame("Duber 2: Electric Boogaloo",
                                                  cheervilleWorld);
    int[] counts = {-1, -1, -1, -1};
    long turns = 0;
    while (true) {
      if (counts[1]+counts[2]+counts[3] != 0) {
        counts = cheervilleWorld.doSimulationStep();
        System.out.printf("Turn %d P:%d H:%d Z:%d\n",
                          turns++, counts[0], counts[1]+counts[2], counts[3]);
      } else {
        counts = cheervilleWorld.resetAndCount();
      }
      display.refresh();
      Thread.sleep(50);
    }
  }
}