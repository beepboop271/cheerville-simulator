import java.util.LinkedList;

public class World {
  private static final double PLANT_SPAWN_CHANCE = 0.05;

  private static final int VISION_SIZE = 4;
  private static final int[][] VISION_OFFSETS = {
    {0, 0, 0, 0},    //nothing
    {-VISION_SIZE, -VISION_SIZE, (2*VISION_SIZE)+1, VISION_SIZE+1},      // scan 9x5 from (x-4, y-4) to (x+4, y) - NORTH
    {0           , -VISION_SIZE, VISION_SIZE+1    , (2*VISION_SIZE)+1},  // scan 5x9 from (x, y-4) to (x+4, y+4) - EAST
    {-VISION_SIZE, 0           , (2*VISION_SIZE)+1, VISION_SIZE+1},      // scan 9x5 from (x-4, y) to (x+4, y+4) - SOUTH
    {-VISION_SIZE, -VISION_SIZE, VISION_SIZE+1    , (2*VISION_SIZE)+1}   // scan 5x9 from (x-4, y-4) to (x, y+4) - WEST
  };

  private int historyAmount = 300;
  private LinkedList<int[]> distributionHistory = new LinkedList<int[]>();
  private int[][] historyArray;

  private final int WIDTH, HEIGHT;
  private Spawnable[][] map;

  public World(int width, int height, int numHumans, int numZombies) {
    this.WIDTH = width;
    this.HEIGHT = height;
    this.map = new Spawnable[height][width];
    this.doSimulationStep();  // let some plants grow
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
              if (!this.isInWorld(newPos[0], newPos[1])) {
                moveAttempts = 0;
                do {
                  newPos = ((Moveable)s).generateRandomMove();
                } while (!this.isInWorld(newPos[0], newPos[1])
                         && (moveAttempts++ < 5));
              }

              if (this.isInWorld(newPos[0], newPos[1])
                    && !((newPos[0] == x) && (newPos[1] == y))) {
                newSpawnable = ((Moveable)s).act(this.getMapAt(newPos[0],
                                                               newPos[1]));
                if (newSpawnable == null) {
                  this.removeMapAt(s);
                  s.setPos(newPos[0], newPos[1]);
                  this.setMapAt(s);
                } else if (newSpawnable.getHealth() <= 0) {
                  this.removeMapAt(s);
                } else if (newSpawnable != s) {
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
                ((Human)s).setStepsUntilFertile(((Human)s).getStepsUntilFertile()-1);
                ((Human)s).incrementAge();
              }
            }
          }
        } else if (Math.random() < PLANT_SPAWN_CHANCE) {
          this.setMapAt(new Plant(x, y));
        }
      }
    }
    return this.resetAndCount();
  }

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

  public Spawnable[][] getVision(Moveable viewer) {
    int facingDirection = viewer.getFacingDirection();
    int[] offsets = VISION_OFFSETS[facingDirection];
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
        // p.act(this.getMapAt(newX, newY));
        p.setPos(newX, newY);
        this.setMapAt(p);
      }
    }
  }

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
    this.historyArray = this.distributionHistory.toArray(new int[0][0]);
  }

  public int[][] getDistributionHistory() {
    return this.historyArray;
  }

  public int getHistoryAmount() {
    return this.historyAmount;
  }

  public void setHistoryAmount(int historyAmount) {
    this.historyAmount = historyAmount;
    while (this.distributionHistory.size() > this.historyAmount) {
      this.distributionHistory.removeFirst();
    }
  }

  public int getVisionSize() {
    return World.VISION_SIZE;
  }

  public int[] getVisionOffsets(int direction) {
    return World.VISION_OFFSETS[direction];
  }

  public int getWidth() {
    return this.WIDTH;
  }

  public int getHeight() {
    return this.HEIGHT;
  }

  public boolean isInWorld(int x, int y) {
    return ((x >= 0) && (x < this.WIDTH) && (y >= 0) && (y < this.HEIGHT));
  }

  public boolean isOccupiedAt(int x, int y) {
    return this.map[y][x] != null;
  }

  public void removeMapAt(Spawnable s) {
    this.map[s.getY()][s.getX()] = null;
  }

  public boolean hasMoveableAt(int x, int y) {
    return this.isOccupiedAt(x, y) && (this.map[y][x] instanceof Moveable);
  }

  public Spawnable getMapAt(int x, int y) {
    return this.map[y][x];
  }

  public void setMapAt(Spawnable s) {
    this.map[s.getY()][s.getX()] = s;
  }
}