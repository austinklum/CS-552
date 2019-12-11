// History.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;

/**
 * Record of all moves played this game.
 */
public interface History {

  /**
   * Return the number of moves so far.
   */
  public int moves();

  /**
   * Return the ID of the player taking action in a particular turn.
   */
  public Object player(int turn);

  /**
   * Return the details of the event occurring at a particular turn.
   */
  public GameEvent getEvent(int turn);
}

