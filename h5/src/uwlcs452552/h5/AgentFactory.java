// AgentFactory.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;

/**
 * Container for the method with which the game controller can create
 * a game-playing agent.
 */
public interface AgentFactory {
  /**
   * Return a new game-playing agent.
   * @param boardSize The number of tile slots along any side of the
   * game board.
   * @param deck The tiles which will be in play during this game.
   * This array is guaranteed to have size (boardSize*boardSize)-1,
   * and will contain duplicates if the same tile appears more than
   * once in the deck.
   * @param maxInitSeconds The maximum number of seconds which this
   * method may use to initialize an agent.  If the factory takes more
   * time than this limit to return an agent, then the agent forfeits
   * the game.
   * @param maxDrawSeconds The maximum number of seconds which an
   * agent may use to react to receiving a card from the deck.  If the
   * agent takes more time than this limit, it will forfeit the game.
   * @param maxTurnSeconds The maximum number of seconds which an
   * agent may use to return a move for its turn.  If the agent takes
   * more time than this limit, it will forfeit the game.
   * @param id A unique identifier assigned by the game controller to
   * this agent.
   */
  public Agent getAgent(int boardSize, Tile[] deck, int maxInitSeconds,
                        int maxDrawSeconds, int maxTurnSeconds, Object id);
}
