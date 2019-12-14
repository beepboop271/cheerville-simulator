/**
 * [CheervilleManager]
 * Holds a World and CheervilleFrame, as well as some
 * settings. Runs and resets the program.
 * 2019-12-13
 * @version 1.2
 * @author Kevin Qiao
 */
public class CheervilleManager {
  private static final int DEFAULT_DELAY = 50;
  private static int delay = CheervilleManager.DEFAULT_DELAY;

  private static final int DEFAULT_INITIAL_HUMANS = 400;
  private static int initialHumans = CheervilleManager.DEFAULT_INITIAL_HUMANS;
  
  private static final int DEFAULT_INITIAL_ZOMBIES = 0;
  private static int initialZombies = CheervilleManager.DEFAULT_INITIAL_ZOMBIES;

  private static final int DEFAULT_WIDTH = 35;
  private int width = CheervilleManager.DEFAULT_WIDTH;

  private static final int DEFAULT_HEIGHT = 35;
  private int height = CheervilleManager.DEFAULT_HEIGHT;

  private World worldToRun;
  private CheervilleFrame display;
  private boolean running = false;


  /** 
   * [CheervilleManager]
   * Constructs an object which holds a World and JFrame for 
   * the Cheerville simulation.
   */
  public CheervilleManager() {
    this.worldToRun = new World(this.width,
                                this.height,
                                CheervilleManager.initialHumans,
                                CheervilleManager.initialZombies);
    this.display = new CheervilleFrame("Duber 2: Electric Boogaloo", this);
  }


  /**
   * [run]
   * Runs the simulation and refreshes the display.
   */
  public void run() {
    while(true) {
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
        this.display.refresh();
        try {
          Thread.sleep(CheervilleManager.delay);
        } catch (InterruptedException e) {}
      }
      this.display.refresh();
      try {
        Thread.sleep(15);
      } catch (InterruptedException e) {}
    }
  }

  
  /**
   * [reset]
   * Clears and re-generates the simulation World.
   */
  public void reset() {
    this.getWorldToRun().reset(CheervilleManager.initialHumans,
                               CheervilleManager.initialZombies);
  }


  /** 
   * [getDefaultDelay]
   * Returns the default time in milliseconds to wait
   * between each simulation step.
   * @return int, the default time in ms to wait between simulation steps.
   */
  public static int getDefaultDelay() {
    return CheervilleManager.DEFAULT_DELAY;
  }

  
  /** 
   * [setDelay]
   * Sets the time in milliseconds to wait between each
   * simulation step.
   * @param delay The time in ms to wait between simulation steps.
   */
  public static void setDelay(int delay) {
    CheervilleManager.delay = delay;
  }

  
  /** 
   * [getWorldToRun]
   * Returns the World which is being simulated and displayed
   * in this manager.
   * @return World, the World being managed.
   */
  public World getWorldToRun() {
    return this.worldToRun;
  }

  
  /** 
   * [isRunning]
   * Returns whether or not the simulation is running.
   * @return boolean, whether or not the simulation is running.
   */
  public boolean isRunning() {
    return this.running;
  }

  
  /** 
   * [setRunning]
   * Sets whether or not the simulation is running.
   * @param running true to run the simulation, false otherwise.
   */
  public void setRunning(boolean running) {
    this.running = running;
  }

  
  /** 
   * [getDefaultInitialHumans]
   * Returns the default number of attempts to spawn a Human
   * when starting the simulation.
   * @return int, the default number of attempts to spawn a Human
   *         at simulation start.
   */
  public static int getDefaultInitialHumans() {
    return CheervilleManager.DEFAULT_INITIAL_HUMANS;
  }

  
  /** 
   * [setInitialHumans]
   * Sets the number of attempts to spawn a Human when 
   * starting the simulation.
   * @param initialHumans The number of attempts to spawn a Human at
   *                      simulation start.
   */
  public static void setInitialHumans(int initialHumans) {
    CheervilleManager.initialHumans = initialHumans;
  }

  
  /** 
   * [getDefaultInitialZombies]
   * Returns the default number of attempts to spawn a Zombie when
   * starting the simulation.
   * @return int, the default number of attempts to spawn a Zombie
   *         at simulation start.
   */
  public static int getDefaultInitialZombies() {
    return CheervilleManager.DEFAULT_INITIAL_ZOMBIES;
  }

  
  /** 
   * [setInitialZombies]
   * Sets the number of attempts to spawn a Zombie when
   * starting the simulation.
   * @param initialZombies The number of attempts to spawn a Zombie at
   *                       simulation start.
   */
  public static void setInitialZombies(int initialZombies) {
    CheervilleManager.initialZombies = initialZombies;
  }

  
  /** 
   * [getDefaultWidth]
   * Returns the default width, in number of cells, of the
   * World to simulate.
   * @return int, the default width of the World in number of cells.
   */
  public static int getDefaultWidth() {
    return CheervilleManager.DEFAULT_WIDTH;
  }

  
  /** 
   * [setWidth]
   * Sets the width of the World to simulate in number of cells
   * and updates the window.
   * @param width The new width of the World, in number of cells.
   */
  public void setWidth(int width) {
    this.width = width;
    this.worldToRun.resize(this.width, this.height);
    this.display.updateWorldSize(this.width, this.height);
  }

  
  /** 
   * [getDefaultHeight]
   * Returns the default height, in number of cells, of the
   * World to simulate.
   * @return int, the default height of the World in number of cells.
   */
  public static int getDefaultHeight() {
    return CheervilleManager.DEFAULT_HEIGHT;
  }

  
  /** 
   * [setHeight]
   * Sets the height of the World to simulate in number of cells
   * and updates the window.
   * @param height The new height of the world, in number of cells.
   */
  public void setHeight(int height) {
    this.height = height;
    this.worldToRun.resize(this.width, this.height);
    this.display.updateWorldSize(this.width, this.height);
  }
}