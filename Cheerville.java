public class Cheerville {
  public static void main(String[] args) throws InterruptedException {
    World cheervilleWorld = new World(10, 10, 5, 0);
    CheervilleFrame display = new CheervilleFrame("Duber 2: Electric Boogaloo",
                                                  cheervilleWorld);
    int[] counts = {-1, -1, -1, -1};
    while(counts[1]+counts[2]+counts[3] != 0) {
      counts = cheervilleWorld.doSimulationStep();
      // System.out.printf("P:%d H:%d Z:%d\n",
      //                   counts[0], counts[1]+counts[2], counts[3]);
      display.refresh();
      Thread.sleep(250);
    }
    // display.dispose();
  }
}