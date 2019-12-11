// TileEvent.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;
import java.util.Set;

/**
 *  {@link History} events in which a {@link Tile} was played for the
 *  agent's {@linkplain Move move}.
 */
public class TileEvent extends GameEvent {
  private final Move move;
  public TileEvent(Object id, Move move, Board before, Board after,
                   Set<Object> eliminated) {
    super(id, before, after, eliminated);
    this.move = move;
  }

/**
 *  Returns the {@link Move} (that is, the {@link Tile} and rotation)
 *  played on a turn.
 */
  public final Move getMove() { return move; }
}

