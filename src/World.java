import java.util.LinkedList;

public class World {
  private static final double DEFAULT_PLANT_SPAWN_CHANCE = 0.05;
  private static double plantSpawnChance = World.DEFAULT_PLANT_SPAWN_CHANCE;

  private static final int DEFAULT_VISION_SIZE = 4;
  private static int visionSize = World.DEFAULT_VISION_SIZE;

  private static int[][] visionOffsets = {
    {0, 0, 0, 0},    //nothing
    {-visionSize, -visionSize, (2*visionSize)+1, visionSize+1},      // scan 9x5 from (x-4, y-4) to (x+4, y) - NORTH
    {0          , -visionSize, visionSize+1    , (2*visionSize)+1},  // scan 5x9 from (x, y-4) to (x+4, y+4) - EAST
    {-visionSize, 0          , (2*visionSize)+1, visionSize+1},      // scan 9x5 from (x-4, y) to (x+4, y+4) - SOUTH
    {-visionSize, -visionSize, visionSize+1    , (2*visionSize)+1}   // scan 5x9 from (x-4, y-4) to (x, y+4) - WEST
  };

  private int historyAmount = 300;
  private LinkedList<int[]> distributionHistory = new LinkedList<int[]>();
  private int[][] historyArray;

  private final int WIDTH, HEIGHT;
  private Spawnable[][] map;


  public World(int width, int height) {
    this.WIDTH = width;
    this.HEIGHT = height;
    this.map = new Spawnable[height][width];
    this.doSimulationStep();  // let some plants grow
  }
  
  /** 
   * [World]
   * Constructor for a World with the given dimensions,
   * starting Humans, and starting Zombies.
   * @param width      The width, in cells, of the World.
   * @param height     The height, in cells, of the World.
   * @param numHumans  The initial number of Humans.
   * @param numZombies The initial number of Zombies.
   */
  public World(int width, int height, int numHumans, int numZombies) {
    this(width, height);
    int x, y;
    for (int i = 0; i < numHumans; ++i) {
      x = (int)(Math.random()*width);
      y = (int)(Math.random()*height);
      this.setMapAt(Human.createHuman(x, y));
    }
    for (int i = 0; i < numZombies; ++i) {
      x = (int)(Math.random()*width);
      y = (int)(Math.random()*height);
      this.setMapAt(new Zombie(x, y));
    }
  }


  public void reset(int numHumans, int numZombies) {
    for (int y = 0; y < this.map.length; ++y) {
      for (int x = 0; x < this.map[0].length; ++x) {
        if (this.isOccupiedAt(x, y)) {
          this.getMapAt(x, y).setDead();
        }
      }
    }
    this.map = new Spawnable[this.HEIGHT][this.WIDTH];
    this.doSimulationStep();
    int x, y;
    for (int i = 0; i < numHumans; ++i) {
      x = (int)(Math.random()*this.WIDTH);
      y = (int)(Math.random()*this.HEIGHT);
      this.setMapAt(Human.createHuman(x, y));
    }
    for (int i = 0; i < numZombies; ++i) {
      x = (int)(Math.random()*this.WIDTH);
      y = (int)(Math.random()*this.HEIGHT);
      this.setMapAt(new Zombie(x, y));
    }
  }

  
  /** 
   * [doSimulationStep]
   * Runs the simulation.
   * @return int[], the current counts of Spawnables.
   */
  public int[] doSimulationStep() {
    Spawnable s;
    int[] newPos;
    Spawnable newSpawnable;
    int moveAttempts;
    for (int y = 0; y < this.HEIGHT; ++y) {
      for (int x = 0; x < this.WIDTH; ++x) {
        if (this.isOccupiedAt(x, y)) {
          s = this.getMapAt(x, y);

          if (s instanceof Plant) {
            if (s.decay() <= 0) {
              this.removeMapAt(s);
            } else {
              // let some plants spread
              Plant newPlant = (Plant)(s.act(null));
              if (newPlant != null) {
                this.growPlantNear(newPlant);
              }
            }
          } else if ((s instanceof Moveable)
                     && !(((Moveable)s).getMoved())) {
            ((Moveable)s).setMoved(true);

            if (s.decay() <= 0) {
              this.removeMapAt(s);
            } else {
              ((Moveable)s).setVision(this.getVision((Moveable)s));
              newPos = ((Moveable)s).generateSmartMove();
              // use random moves if the smart move is not valid
              if (!this.isInWorld(newPos[0], newPos[1])) {
                moveAttempts = 0;
                do {
                  newPos = ((Moveable)s).generateRandomMove();
                } while (!this.isInWorld(newPos[0], newPos[1])
                         && (moveAttempts++ < 5));
              }

              // if the move is not valid still just don't move
              if (this.isInWorld(newPos[0], newPos[1])
                    && !((newPos[0] == x) && (newPos[1] == y))) {
                newSpawnable = ((Moveable)s).act(this.getMapAt(newPos[0],
                                                               newPos[1]));
                if (newSpawnable == null) {
                  // null -> nothing, allow the movement
                  this.removeMapAt(s);
                  s.setPos(newPos[0], newPos[1]);
                  this.setMapAt(s);
                } else if (newSpawnable.getHealth() <= 0) {
                  this.removeMapAt(s);
                } else if (newSpawnable != s) {  // newSpawnable == s -> don't move
                  if (newSpawnable instanceof Zombie) {
                    // zombies only spawn here if they replace a human
                    // so just replace the map spot with the human
                    this.setMapAt(newSpawnable);
                  } else if (newSpawnable instanceof Human) {
                    // humans do not replace existing things,
                    // they are added into the world
                    this.spawnHumanNear((Human)newSpawnable);
                  }
                }
              }

              if (s instanceof Human) {
                // some things still happen even if no movement is made
                ((Human)s).setStepsUntilFertile(((Human)s).getStepsUntilFertile()-1);
                ((Human)s).incrementAge();
              }
            }
          }
        } else if (Math.random() < World.plantSpawnChance) {
          // unoccupied tile can sometimes create a Plant
          this.setMapAt(new Plant(x, y));
        }
      }
    }
    return this.resetAndCount();
  }

  
  /** 
   * [resetAndCount]
   * Clears the moved value of all Moveables and counts
   * how many Spawnables there are.
   * @return int[], the current counts of Spawnables.
   */
  public int[] resetAndCount() {
    Spawnable s;
    int[] counts = {0, 0, 0, 0};
    for (int y = 0; y < this.HEIGHT; ++y) {
      for (int x = 0; x < this.WIDTH; ++x) {
        if (this.isOccupiedAt(x, y)) {
          s = this.getMapAt(x, y);
          if (s instanceof Plant) {
            ++counts[0];
          } else if (s instanceof Moveable) {
            ((Moveable)s).setMoved(false);
            if (s instanceof Human) {
              if (s instanceof Female) {
                ++counts[1];
              } else {
                ++counts[2];
              }
            } else if (s instanceof Zombie) {
              ++counts[3];
            }
          }
        }
      }
    }
    this.addHistory(counts);
    return counts;
  }

  
  /** 
   * [getVision]
   * Return a section of this World that is visible
   * to the viewer.
   * @param viewer The Moveable looking at the World.
   * @return Spawnable[][], the section of the World visible.
   */
  public Spawnable[][] getVision(Moveable viewer) {
    int facingDirection = viewer.getFacingDirection();
    int[] offsets = visionOffsets[facingDirection];
    Spawnable[][] vision = new Spawnable[offsets[3]][offsets[2]];
    for (int i = 0; i < offsets[3]; ++i) {
      for (int j = 0; j < offsets[2]; ++j) {
        if (this.isInWorld(viewer.getX()+j+offsets[0],
                           viewer.getY()+i+offsets[1])) {
          vision[i][j] = this.getMapAt(viewer.getX()+j+offsets[0],
                                       viewer.getY()+i+offsets[1]);
        }
      }
    }
    return vision;
  }

  
  /** 
   * [spawnHumanNear]
   * Attempt to place a Human near its (x, y) position.
   * @param h The Human to place.
   */
  public void spawnHumanNear(Human h) {
    int x = h.getX();
    int y = h.getY();
    int[][] possibleLocations = {
      {x+1, y}, {x, y+1}, {x-1, y}, {x, y-1},
      {x+1, y+1}, {x-1, y+1}, {x-1, y-1}, {x+1, y-1}
    };
    int newX, newY;
    for (int i = 0; i < possibleLocations.length; ++i) {
      newX = possibleLocations[i][0];
      newY = possibleLocations[i][1];
      if (this.isInWorld(newX, newY) && !this.hasMoveableAt(newX, newY)) {
        h.act(this.getMapAt(newX, newY));
        h.setPos(newX, newY);
        this.setMapAt(h);
        return;
      }
    }
  }

  /** 
   * [spawnHumanNear]
   * Attempt to place a new Human near the given (x, y).
   * @param x The x coordinate to place the new Human near.
   * @param y The y coordinate to place the new Human near.
   */
  public void spawnHumanNear(int x, int y) {
    int[][] possibleLocations = {
      {x+1, y}, {x, y+1}, {x-1, y}, {x, y-1},
      {x+1, y+1}, {x-1, y+1}, {x-1, y-1}, {x+1, y-1}
    };
    int newX, newY;
    for (int i = 0; i < possibleLocations.length; ++i) {
      newX = possibleLocations[i][0];
      newY = possibleLocations[i][1];
      if (this.isInWorld(newX, newY) && !this.hasMoveableAt(newX, newY)) {
        Human h = Human.createHuman(newX, newY);
        h.act(this.getMapAt(newX, newY));
        h.setPos(newX, newY);
        this.setMapAt(h);
        return;
      }
    }
  }

  
  /** 
   * [spawnZombieNear]
   * Attempt to place a new Zombie near the given (x, y).
   * @param x The x coordinate to place the new Zombie near.
   * @param y The y coordinate to place the new Zombie near.
   */
  public void spawnZombieNear(int x, int y) {
    int[][] possibleLocations = {
      {x, y}, {x+1, y}, {x, y+1}, {x-1, y}, {x, y-1},
      {x+1, y+1}, {x-1, y+1}, {x-1, y-1}, {x+1, y-1}
    };
    int newX, newY;
    for (int i = 0; i < possibleLocations.length; ++i) {
      newX = possibleLocations[i][0];
      newY = possibleLocations[i][1];
      if (this.isInWorld(newX, newY) && !this.hasMoveableAt(newX, newY)) {
        this.setMapAt(new Zombie(newX, newY));
        return;
      }
    }
  }

  
  /** 
   * [growPlantNear]
   * Attempt to place a Plant near its (x, y) position.
   * @param p The Plant to place.
   */
  public void growPlantNear(Plant p) {
    int x = p.getX();
    int y = p.getY();
    int[][] possibleLocations = {
      {x, y}, {x+1, y}, {x, y+1}, {x-1, y}, {x, y-1}
    };
    int idx;
    int newX, newY;
    int attempts = 0;
    do {
      idx = (int)(Math.random()*possibleLocations.length);
      newX = possibleLocations[idx][0];
      newY = possibleLocations[idx][1];
    } while ((!this.isInWorld(newX, newY)
              || this.hasMoveableAt(newX, newY))
             && (attempts++ < possibleLocations.length));
    if (this.isInWorld(newX, newY) && !this.hasMoveableAt(newX, newY)) {
      if (this.isOccupiedAt(newX, newY)) {
        this.getMapAt(newX, newY).act(p);
      } else {
        p.setPos(newX, newY);
        this.setMapAt(p);
      }
    }
  }

  
  /** 
   * [selectRandomZombie]
   * Find a random Zombie by flood filling from a random
   * location until a Zombie is found or the whole map has been filled.
   * @return Zombie, null if no Zombie was found or the
   *         first Zombie that is found.
   */
  public Zombie selectRandomZombie() {
    int[] start = {
      (int)(Math.random()*this.getWidth()),
      (int)(Math.random()*this.getHeight())
    };

    LinkedList<int[]> queue = new LinkedList<int[]>();
    queue.addLast(start);

    boolean[][] visited = new boolean[this.getHeight()][this.getWidth()];

    int[] current;
    int[] next = new int[2];
    while (!queue.isEmpty()) {
      current = queue.removeFirst();
      if (this.getMapAt(current[0], current[1]) instanceof Zombie) {
        return (Zombie)(this.getMapAt(current[0], current[1]));
      } else {
        for (int i = 1; i <= 4; ++i) {
          if (this.isInWorld(current[0]+Cheerville.MOVEMENTS[i][0],
                             current[1]+Cheerville.MOVEMENTS[i][1])
                && !visited[current[1]+Cheerville.MOVEMENTS[i][1]]
                           [current[0]+Cheerville.MOVEMENTS[i][0]]) {
            next[0] = current[0]+Cheerville.MOVEMENTS[i][0];
            next[1] = current[1]+Cheerville.MOVEMENTS[i][1];
            visited[next[1]][next[0]] = true;
            queue.addLast(next.clone());
          }
        }
      }
    }
    return null;
  }

  
  /** 
   * [selectRandomHuman]
   * Find a random Human by flood filling from a random
   * location until a Human is found or the whole map has been filled.
   * @return Human, null if no Human was found or the
   *         first Human that is found.
   */
  public Human selectRandomHuman() {
    int[] start = {
      (int)(Math.random()*this.getWidth()),
      (int)(Math.random()*this.getHeight())
    };

    LinkedList<int[]> queue = new LinkedList<int[]>();
    queue.addLast(start);

    boolean[][] visited = new boolean[this.getHeight()][this.getWidth()];

    int[] current;
    int[] next = new int[2];
    while (!queue.isEmpty()) {
      current = queue.removeFirst();
      if (this.getMapAt(current[0], current[1]) instanceof Human) {
        return (Human)(this.getMapAt(current[0], current[1]));
      } else {
        for (int i = 1; i <= 4; ++i) {
          if (this.isInWorld(current[0]+Cheerville.MOVEMENTS[i][0],
                             current[1]+Cheerville.MOVEMENTS[i][1])
                && !visited[current[1]+Cheerville.MOVEMENTS[i][1]]
                           [current[0]+Cheerville.MOVEMENTS[i][0]]) {
            next[0] = current[0]+Cheerville.MOVEMENTS[i][0];
            next[1] = current[1]+Cheerville.MOVEMENTS[i][1];
            visited[next[1]][next[0]] = true;
            queue.addLast(next.clone());
          }
        }
      }
    }
    return null;
  }

  
  /** 
   * [addHistory]
   * Adds a Spawnable count record to the history, removing
   * old records if the history limit is reached.
   * @param counts The Spawnable count record to add.
   */
  public void addHistory(int[] counts) {
    int[] record = {
      counts[0],  // plants
      counts[1],  // females
      counts[2],  // males
      counts[3],  // zombies
      (this.WIDTH*this.HEIGHT)-counts[0]-counts[1]-counts[2]-counts[3]
    };
    this.distributionHistory.addLast(record);
    while (this.distributionHistory.size() > this.getHistoryAmount()) {
      this.distributionHistory.removeFirst();
    }

    // swing is dumb and i cannot use linked lists
    this.historyArray = this.distributionHistory.toArray(new int[0][0]);
  }
  

  public static double getDefaultPlantSpawnChance() {
    return World.DEFAULT_PLANT_SPAWN_CHANCE;
  }

  public static void setPlantSpawnChance(double plantSpawnChance) {
    World.plantSpawnChance = plantSpawnChance;
  }

  
  /** 
   * [getVisionSize]
   * Returns the distance in cells that Moveables can see.
   * @return int, the distance that Moveables can see.
   */
  public static int getVisionSize() {
    return World.visionSize;
  }


  public static int getDefaultVisionSize() {
    return World.DEFAULT_VISION_SIZE;
  }

  
  /** 
   * [setVisionSize]
   * Sets the distance in cells that Moveables can see.
   * @return int, the distance that Moveables can see.
   */
  public static void setVisionSize(int visionSize) {
    World.visionSize = visionSize;
    World.updateVisionOffsets();
  }

  
  /** 
   * [getVisionOffsets]
   * Returns the numbers which specify the rectangle of
   * vision a Moveable has based on the direction it is facing.
   * @param direction The direction the Moveable is facing.
   * @return int[], integers specifying a rectangle around the
   *         Moveable that is visible.
   */
  public static int[] getVisionOffsets(int direction) {
    return World.visionOffsets[direction];
  }


  public static void updateVisionOffsets() {
    int[][] visionOffsets = {
      {0, 0, 0, 0},
      {-World.visionSize, -World.visionSize, (2*World.visionSize)+1, World.visionSize+1},
      {0                , -World.visionSize, World.visionSize+1    , (2*World.visionSize)+1},
      {-World.visionSize, 0                , (2*World.visionSize)+1, World.visionSize+1},
      {-World.visionSize, -World.visionSize, World.visionSize+1    , (2*World.visionSize)+1}
    };
    World.visionOffsets = visionOffsets;
  }

  
  /** 
   * [getDistributionHistory]
   * Returns the Spawnable counts over time.
   * @return int[][], the Spawnable counts over time.
   */
  public int[][] getDistributionHistory() {
    return this.historyArray;
  }

  
  /** 
   * [getHistoryAmount]
   * Returns the maximum amount of records to keep.
   * @return int, the maximum amount of records to keep.
   */
  public int getHistoryAmount() {
    return this.historyAmount;
  }

  
  /** 
   * [setHistoryAmount]
   * Sets the maximum amount of records to keep and
   * deletes records if necessary until the new limit
   * is satisfied.
   * @param historyAmount The new maximum amount of records to keep.
   */
  public void setHistoryAmount(int historyAmount) {
    this.historyAmount = historyAmount;
    while (this.distributionHistory.size() > this.historyAmount) {
      this.distributionHistory.removeFirst();
    }
  }

  
  /** 
   * [getWidth]
   * Returns the width of the World.
   * @return int, the width of the World.
   */
  public int getWidth() {
    return this.WIDTH;
  }

  
  /** 
   * [getHeight]
   * Returns the height of the World.
   * @return int, the height of the World.
   */
  public int getHeight() {
    return this.HEIGHT;
  }

  
  /** 
   * [isInWorld]
   * Checks if the given point is within this World.
   * @param x The x coordinate of the point to check.
   * @param y The y coordinate of the point to check.
   * @return boolean, whether or not the point is within the World.
   */
  public boolean isInWorld(int x, int y) {
    return ((x >= 0) && (x < this.WIDTH) && (y >= 0) && (y < this.HEIGHT));
  }

  
  /** 
   * [isOccupiedAt]
   * Checks if the given point in the World has anything on it.
   * @param x The x coordinate of the point to check.
   * @param y The y coordinate of the point to check.
   * @return boolean, whether or not the specified point in the
   *         World contains anything on it.
   */
  public boolean isOccupiedAt(int x, int y) {
    return this.map[y][x] != null;
  }

  
  /** 
   * [removeMapAt]
   * Removes a Spawnable from the World by setting the map
   * at the Spawnable's position to null.
   * @param s The Spawnable to remove.
   */
  public void removeMapAt(Spawnable s) {
    this.map[s.getY()][s.getX()] = null;
  }

  
  /** 
   * [hasMoveableAt]
   * Checks if the given point in the World has a Moveable on it.
   * @param x The x coordinate of the point to check.
   * @param y The y coordinate of the point to check.
   * @return boolean, whether or not the specified point in the
   *         World contains a Moveable on it.
   */
  public boolean hasMoveableAt(int x, int y) {
    return this.isOccupiedAt(x, y) && (this.map[y][x] instanceof Moveable);
  }

  
  /** 
   * [getMapAt]
   * Returns the Spawnable at the given point in the World
   * or null if the point is unoccupied.
   * @param x The x coordinate of the point to query.
   * @param y The y coordinate of the point to query.
   * @return Spawnable, the Spawnable at the given point
   *         or null if the point is unoccupied.
   */
  public Spawnable getMapAt(int x, int y) {
    return this.map[y][x];
  }

  
  /** 
   * [setMapAt]
   * Sets the map at the given Spawnable's position to that
   * Spawnable.
   * @param s The Spawnable to set.
   */
  public void setMapAt(Spawnable s) {
    this.map[s.getY()][s.getX()] = s;
  }
}