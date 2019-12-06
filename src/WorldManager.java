public class WorldManager {
  World worldToRun;

  public WorldManager(int width, int height) {
    this.worldToRun = new World(width, height);
  }

  public void run() {
    int[] counts = {-1, -1, -1, -1};
    long turns = 0;
    while (true) {
      if (counts[1]+counts[2]+counts[3] != 0) {
        
      }
    }
  }
}