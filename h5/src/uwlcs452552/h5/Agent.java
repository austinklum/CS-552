// Agent.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;

/**
 * Methods which the game controller expects an agent to provide.
 */
public interface Agent {

  /**
   * Return the move by this agent for a particular board.
   * @param board The current state of the game.
   * @param history A record of the moves played so far.
   */
  public Move play(Board board, History history);

  /**
   * Notify the agent that it should add a tile to its hand.
   * @param tile The tile added in.
   */
  public void receiveTile(Tile tile);

}
