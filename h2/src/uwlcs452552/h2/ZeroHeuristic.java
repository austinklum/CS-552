package uwlcs452552.h2;

/**
 *  An example of extending {@link MovesFinder} with a heuristic
 *  function.  The heuristic here just returns zero for every state;
 *  your heuristics should beat this one as thoroughly as they beat
 *  BFS.
 */
public class ZeroHeuristic extends MovesFinder {
  public ZeroHeuristic() {
    super((b) -> 0.0);
  }
}
