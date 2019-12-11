// ForfeitEvent.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;
import java.util.Set;

/**
 *  {@link History} events in which an agent forfeited the game.
 */
public class ForfeitEvent extends GameEvent {
  public ForfeitEvent(Object id, Board before, Board after,
                      Set<Object> eliminated) {
    super(id, before, after, eliminated);
  }
}

