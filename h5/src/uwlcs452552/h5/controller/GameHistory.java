// GameHistory.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5.controller;
import java.util.Collections;
import java.util.Set;
import uwlcs452552.h5.History;
import uwlcs452552.h5.Board;
import uwlcs452552.h5.Move;
import uwlcs452552.h5.GameEvent;
import uwlcs452552.h5.TileEvent;
import uwlcs452552.h5.ForfeitEvent;

/**
 *  Package-hidden implementation of {@link History}.
 */
class GameHistory implements History {

  private final GameEvent[] events;
  private int eventCount = 0;

  GameHistory(int agents, int boardSize) {
    this.events = new GameEvent[agents + boardSize*boardSize - 1];
  }

  /** {@inheritDoc} */
  public int moves() { return eventCount; }

  /** {@inheritDoc} */
  public Object player(int turn) {
    if (0<=turn && turn<eventCount)
      return events[turn].getAgentId();
    else
      throw new IllegalArgumentException("Accessed history event " + turn
                                         + " but there are only "
                                         + eventCount + " events so far");
  }

  /** {@inheritDoc} */
  public GameEvent getEvent(int turn) {
    if (0<=turn && turn<eventCount)
      return events[turn];
    else
      throw new IllegalArgumentException("Accessed history event " + turn
                                         + " but there are only "
                                         + eventCount + " events so far");
  }

  void recordTileEvent(Object id, Move move, Board before, Board after,
                       Set<Object> eliminated) {
    events[eventCount++] = new TileEvent
        (id, move, before, after, Collections.unmodifiableSet(eliminated));
  }

  void recordForfeitEvent(Object id, Board before, Board after,
                          Set<Object> eliminated) {
    events[eventCount++] = new ForfeitEvent
        (id, before, after, Collections.unmodifiableSet(eliminated));
  }
}

