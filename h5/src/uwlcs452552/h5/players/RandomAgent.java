// RandomAgent.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5.players;
import java.util.Random;

/**
 *  Common superclass of agents with random play generation.
 */
public abstract class RandomAgent {
  private Random rand = new Random();
  public void setRandom(Random rand) { this.rand = rand; }
  public Random getRandom() { return rand; }
}
