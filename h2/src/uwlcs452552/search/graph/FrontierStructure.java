package uwlcs452552.search.graph;

/**
 *  Methods required of a representation of a search tree frontier.
 *
 *  The {@link Frontiers} class contains some standard implementations
 *  and utilities of this interface.
 *
 * @param <Node> The type of tree nodes stored in the frontier.
 *
 * @see GraphSearcher#search
 * @see Frontiers
 */
public interface FrontierStructure<Node> {

  /**
   *  Adds a (usually newly-generated) tree node to the frontier.
   *
   * @param n The new node
   */
  public void add(Node n);

  /**
   *  Checks whether any tree nodes remain in the frontier
   *
   * @return <tt>false</tt> when the frontier is empty, which
   * generally indicates that the search has failed.
   */
  public boolean isEmpty();

  /**
   *  Removes one tree node from the frontier, and returns it.
   *
   * @return The dequeued tree node
   *
   * @throws IllegalStateException when this method is called but the
   * frontier is empty; the exception may contain a cause if the
   * exception was generated by some other data structure.
   */
  public Node pop();

}
