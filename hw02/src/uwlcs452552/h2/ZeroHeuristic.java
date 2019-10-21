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

  /**
   *  Note that if we override toString() with a nice and distinct
   *  name, it will help the output of {@link AbstractSolution#run} to
   *  be a bit more readable.
   */
  @Override
  public String toString() { return "Zero function"; }
}
