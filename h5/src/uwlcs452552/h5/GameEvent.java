// GameEvent.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;
import java.util.Set;

/**
 *  Superclass of the events that the {@link History} class will
 *  report for each turn.
 */
public abstract class GameEvent {
  private final Object id;
  private final Board before, after;
  private final Set<Object> eliminated;

  protected GameEvent(Object id, Board before, Board after,
                      Set<Object> eliminated) {
    this.id = id;
    this.before = before;
    this.after = after;
    this.eliminated = eliminated;
  }

  /** Return the agent playing at this turn. */
  public Object getAgentId() { return id; }
  /** Return the state of the game {@linkplain Board board} before this turn.
   */
  public Board getBefore() { return before; }
  /** Return the state of the game {@linkplain Board board} after this turn. */
  public Board getAfter() { return after; }
  /** Return the {@link Set} of agents eliminated from the game by this turn.
   */
  public Set<Object> getEliminated() { return eliminated; }
}
