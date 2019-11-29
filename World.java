import java.util.LinkedList;

public class World {
  private static final double PLANT_SPAWN_CHANCE = 0.05;

  private int historyAmount = 300;
  private LinkedList<int[]> distributionHistory = new LinkedList<int[]>();
  private int[][] historyArray;

  private final int WIDTH, HEIGHT;
  private Spawnable[][] map;

  public World(int width, int height, int numHumans, int numZombies) {
    this.WIDTH = width;
    this.HEIGHT = height;
    this.map = new Spawnable[height][width];
    for (int i = 0; i < 25; ++i) {
    // for (int i = 0; i < 5; ++i) {
      this.doSimulationStep();  // let some plants grow
    }
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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(this.HEIGHT*this.WIDTH);
    for (int y = 0; y < this.HEIGHT; ++y) {
      for (int x = 0; x < this.WIDTH; ++x) {
        if (this.isOccupiedAt(x, y)) {
          sb.append(this.getMapAt(x, y).toString());
        } else {
          sb.append(' ');
        }
      }
      sb.append('\n');
    }
    return sb.toString();
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
          // if (!(s instanceof Plant)) {
          //   System.out.println(s.toString());
          //   System.out.println(s.getID());
          //   if (s instanceof Moveable) {
          //     System.out.printf("AT %s %d, (%d, %d)\n", s.toString(), s.getID(),
          //                       s.getX(), s.getY());
          //   }
          // }

          if(s instanceof Plant) {
            if(s.decay() <= 0) {
              this.removeMapAt(s);
            } else {
              Plant newPlant = (Plant)(s.act(null));
              if (newPlant != null) {
                this.growPlantNear(newPlant);
              }
            }
          } else if ((s instanceof Moveable)
                && !((Moveable)s).getMoved()) {
            ((Moveable)s).setMoved(true);
            if(s.decay() <= 0) {
              this.removeMapAt(s);
            } else {
              // System.out.println(s.getHealth());
              ((Moveable)s).setVision(this.getVision((Moveable)s));
              newPos = ((Moveable)s).generateSmartMove();
              if(!this.isInWorld(newPos[0], newPos[1])) {
                moveAttempts = 0;
                do {
                  newPos = ((Moveable)s).generateRandomMove();
                } while (!this.isInWorld(newPos[0], newPos[1])
                         && (moveAttempts++ < 5));
              }

              if(this.isInWorld(newPos[0], newPos[1]) && !(newPos[0] == x && newPos[1] == y)) {
                // System.out.printf("%s at (%d, %d)\n",
                //  this.getMapAt(newPos[0], newPos[1]),
                //  newPos[0], newPos[1]);
                newSpawnable = ((Moveable)s).act(this.getMapAt(newPos[0],
                                                               newPos[1]));
                if (newSpawnable == null) {
                  // System.out.println("nothing happened");
                  this.removeMapAt(s);
                  s.setPos(newPos[0], newPos[1]);
                  this.setMapAt(s);
                } else if (newSpawnable.getHealth() <= 0) {
                  this.removeMapAt(s);
                } else if (newSpawnable == s) {

                } else if (newSpawnable instanceof Zombie) {
                  // zombies only spawn here if they replace a human
                  // so just replace the map spot with the human
                  this.setMapAt(newSpawnable);
                } else if (newSpawnable instanceof Human) {
                  // humans do not replace existing things,
                  // they are added into the world
                  this.spawnHumanNear((Human)newSpawnable);
                }
              }

              if (s instanceof Human) {
                ((Human)s).setStepsUntilFertile(((Human)s).getStepsUntilFertile()-1);
                ((Human)s).incrementAge();
              }
              
            }
            // System.out.println("");
          }
        } else if (Math.random() < PLANT_SPAWN_CHANCE) {
          this.setMapAt(new Plant(x, y));
        }
      }
    }
    return this.resetAndCount();
  }

  private int[] resetAndCount() {
    Spawnable s;
    int[] counts = {0, 0, 0, 0};
    for (int y = 0; y < this.HEIGHT; ++y) {
      for (int x = 0; x < this.WIDTH; ++x) {
        if (this.isOccupiedAt(x, y)) {
          s = this.getMapAt(x, y);
          if(s.getX() != x || s.getY() != y) {
            System.out.println(this.toString());
            System.out.printf("%s at (%d, %d) thought it was at (%d, %d)\n",
                              s.toString(), x, y, s.getX(), s.getY());
            System.out.println("something terrible has happened");
            System.exit(1);
          }
          // System.out.printf("%s %d\n", s.toString(), s.getID());
          if (s instanceof Plant) {
            ++counts[0];
          } else if (s instanceof Moveable) {
            // System.out.printf("%s %d\n", s.toString(), s.getID());
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

  private static final int VISION_SIZE = 4;
  private static final int[][] VISION_OFFSETS = {
    {0, 0, 0, 0},    //nothing
    {-VISION_SIZE, -VISION_SIZE, (2*VISION_SIZE)+1, VISION_SIZE+1},      // scan 9x5 from (x-4, y-4) to (x+4, y) - NORTH
    {0           , -VISION_SIZE, VISION_SIZE+1    , (2*VISION_SIZE)+1},  // scan 5x9 from (x, y-4) to (x+4, y+4) - EAST
    {-VISION_SIZE, 0           , (2*VISION_SIZE)+1, VISION_SIZE+1},      // scan 9x5 from (x-4, y) to (x+4, y+4) - SOUTH
    {-VISION_SIZE, -VISION_SIZE, VISION_SIZE+1    , (2*VISION_SIZE)+1}   // scan 5x9 from (x-4, y-4) to (x, y+4) - WEST
  };
  public Spawnable[][] getVision(Moveable viewer) {
    int facingDirection = viewer.getFacingDirection();
    int[] offsets = VISION_OFFSETS[facingDirection];
    Spawnable[][] vision = new Spawnable[offsets[3]][offsets[2]];
    for(int i = 0; i < offsets[3]; ++i) {
      for(int j = 0; j < offsets[2]; ++j) {
        if(this.isInWorld(viewer.getX()+j+offsets[0], viewer.getY()+i+offsets[1])) {
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
    for(int i = 0; i < possibleLocations.length; ++i) {
      newX = possibleLocations[i][0];
      newY = possibleLocations[i][1];
      if(this.isInWorld(newX, newY) && !this.hasMoveableAt(newX, newY)) {
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
    for(int i = 0; i < possibleLocations.length; ++i) {
      newX = possibleLocations[i][0];
      newY = possibleLocations[i][1];
      if(this.isInWorld(newX, newY) && !this.hasMoveableAt(newX, newY)) {
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
             && attempts++ < possibleLocations.length);
    if(this.isInWorld(newX, newY) && !this.hasMoveableAt(newX, newY)) {
      if(this.isOccupiedAt(newX, newY)) {
        this.getMapAt(newX, newY).act(p);
      } else {
        p.act(this.getMapAt(newX, newY));
        p.setPos(newX, newY);
        this.setMapAt(p);
      }
    }
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
    while(this.distributionHistory.size() > this.getHistoryAmount()) {
      this.distributionHistory.removeFirst();
    }
    this.historyArray = this.distributionHistory.toArray(new int[0][0]);
  }

  // public LinkedList<int[]> getDistributionHistory() {
  //   return this.distributionHistory;
  // }
  public int[][] getDistributionHistory() {
    return this.historyArray;
  }

  public int getHistoryAmount() {
    return this.historyAmount;
  }

  public void setHistoryAmount(int historyAmount) {
    this.historyAmount = historyAmount;
    while(this.distributionHistory.size() > this.historyAmount) {
      this.distributionHistory.removeFirst();
    }
  }

  public int getWidth() {
    return this.WIDTH;
  }

  public int getHeight() {
    return this.HEIGHT;
  }

  public boolean isInWorld(int x, int y) {
    return x >= 0 && x < this.WIDTH && y >= 0 && y < this.HEIGHT;
  }

  public boolean isOccupiedAt(int x, int y) {
    return this.map[y][x] != null;
  }

  public boolean hasMoveableAt(int x, int y) {
    return this.isOccupiedAt(x, y) && (this.map[y][x] instanceof Moveable);
  }

  public Spawnable getMapAt(int x, int y) {
    return this.map[y][x];
  }

  public void setMapAt(int x, int y, Spawnable s) {
    // if(s instanceof Moveable) {
    //   if(this.map[y][x] != null) {
    //     System.out.printf("(%d, %d) replace set %d, %s with %d, %s\n",
    //                       x, y,
    //                       this.map[y][x].getID(), this.map[y][x].toString(),
    //                       s.getID(), s.toString());
    //   } else {
    //     System.out.printf("(%d, %d) set %d, %s\n", x, y, s.getID(), s.toString());
    //   }
    // }
    this.map[y][x] = s;
  }

  public void setMapAt(Spawnable s) {
    // System.out.printf("set world at %d, %d\n", s.getX(), s.getY());
    this.map[s.getY()][s.getX()] = s;
    // System.out.println(this);
  }

  public void removeMapAt(int x, int y) {
    // if(this.map[y][x] != null && this.map[y][x] instanceof Moveable) {
    //   System.out.printf("(%d, %d) remove %d, %s\n",
    //                     x, y,
    //                     this.map[y][x].getID(), this.map[y][x].toString());
    // } else {
    //   System.out.printf("(%d, %d) nothing happened", x, y);
    // }
    this.map[y][x] = null;
  }

  public void removeMapAt(Spawnable s) {
    if(this.getMapAt(s.getX(), s.getY()) != s) {
      System.out.println("hmmmmm");
    }
    // System.out.printf("remove world at %d, %d\n", s.getX(), s.getY());
    this.map[s.getY()][s.getX()] = null;
  }
}