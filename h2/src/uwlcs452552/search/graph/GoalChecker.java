package uwlcs452552.search.graph;
import uwlcs452552.search.SearchFailureException;
import java.util.function.Predicate;

/**
 *  Methods required of objects which check that a tree node
 *  corresponds to a search goal.
 *
 *  This interface extends a {@link java.util.function.Predicate
 *  Predicate}, and its <tt>check</tt> method does correspond to
 *  checking each node as it is removed from the frontier for
 *  expansion.
 *
 *  However these object can also allow the search process to delay
 *  selecting the found node until the end of a complete search; in
 *  this usage the {@link #get get} method should return the desired
 *  search result.
 *
 * @param <Node> Type representing nodes in the search tree.
 */
public interface GoalChecker<Node> extends Predicate<Node> {
  /**
   * When a search exhausts the frontier, this method can return the
   * result, or else should throw a {@link
   * uwlcs452552.search.SearchFailureException
   * SearchFailureException}.
   */
  public Node get() throws SearchFailureException;
}

