public class WorldManager {
  private static final int DEFAULT_DELAY = 50;
  private static int delay = WorldManager.DEFAULT_DELAY;
  private static final int DEFAULT_INITIAL_HUMANS = 400;
  private static int initialHumans = WorldManager.DEFAULT_INITIAL_HUMANS;
  private static final int DEFAULT_INITIAL_ZOMBIES = 0;
  private static int initialZombies = WorldManager.DEFAULT_INITIAL_ZOMBIES;

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


  public void reset() {
    this.getWorldToRun().reset(WorldManager.initialHumans,
                               WorldManager.initialZombies);
  }


  public static int getDefaultDelay() {
    return WorldManager.DEFAULT_DELAY;
  }

  public static void setDelay(int delay) {
    WorldManager.delay = delay;
  }

  public World getWorldToRun() {
    return this.worldToRun;
  }

  public boolean isRunning() {
    return this.running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  public static int getDefaultInitialHumans() {
    return WorldManager.DEFAULT_INITIAL_HUMANS;
  }

  public static void setInitialHumans(int initialHumans) {
    WorldManager.initialHumans = initialHumans;
  }

  public static int getDefaultInitialZombies() {
    return WorldManager.DEFAULT_INITIAL_ZOMBIES;
  }

  public static void setInitialZombies(int initialZombies) {
    WorldManager.initialZombies = initialZombies;
  }

  public static int getWidth() {
    return WorldManager.width;
  }

  public static void setWidth(int width) {
    WorldManager.width = width;
  }

  public static int getHeight() {
    return WorldManager.height;
  }

  public static void setHeight(int height) {
    WorldManager.height = height;
  }
}