public class WorldManager {
  private static int delay = 50;
  private static int initialHumans = 400;
  private static int initialZombies = 0;
  private static int width = 35;
  private static int height = 35;

  private World worldToRun;
  private boolean running = false;


  public WorldManager() {
    this.worldToRun = new World(WorldManager.width,
                                WorldManager.height,
                                WorldManager.initialHumans,
                                WorldManager.initialZombies);
  }

  // public void reset() {
  //   this.worldToRun = new World(WorldManager.width,
  //                               WorldManager.height,
  //                               WorldManager.initialHumans,
  //                               WorldManager.initialZombies);
  // }

  public void run() {
    int[] counts = {-1, -1, -1, -1};
    long turns = 0;
    while (this.running) {
      if (counts[1]+counts[2]+counts[3] != 0) {
        counts = this.worldToRun.doSimulationStep();
        System.out.printf("Turn %d P:%d H:%d Z:%d\n",
                          turns++, counts[0], counts[1]+counts[2], counts[3]);
      } else {
        counts = this.worldToRun.resetAndCount();
      }
      try {
        Thread.sleep(WorldManager.delay);
      } catch (InterruptedException e) {
      }

    }
  }

  public static int getDelay() {
    return delay;
  }

  public static void setDelay(int delay) {
    WorldManager.delay = delay;
  }

  public World getWorldToRun() {
    return worldToRun;
  }

  public boolean isRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  public static int getInitialHumans() {
    return initialHumans;
  }

  public static void setInitialHumans(int initialHumans) {
    WorldManager.initialHumans = initialHumans;
  }

  public static int getInitialZombies() {
    return initialZombies;
  }

  public static void setInitialZombies(int initialZombies) {
    WorldManager.initialZombies = initialZombies;
  }

  public static int getWidth() {
    return width;
  }

  public static void setWidth(int width) {
    WorldManager.width = width;
  }

  public static int getHeight() {
    return height;
  }

  public static void setHeight(int height) {
    WorldManager.height = height;
  }
}