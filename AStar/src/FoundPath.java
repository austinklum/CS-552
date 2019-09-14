import java.util.List;
import java.util.Optional;

/**
 * Bundle of information returned by the primary search method of a
 * {@link Pathfinder} instance.
 */
public interface FoundPath {
  /** Returns the sequence of cities at the travel leg in the best
   * path between a departure city and destination city.
   * @returns The first element of this list should be the intended
   * departure city, and the last element of this list should be the
   * intended destination.  This method should return an empty list if
   * no path was possible.
   */
  public List<String> getPath();

  /** Returns the total cost of the search path, or an empty {@link
   * java.util.Optional} instance if no path is possible.
   */
  public Optional<Integer> getTotalCost();

  /** Returns the total number of nodes which A* generated and placed
   * into the frontier in the search.
   */
  public int totalNodes();

  /** Returns the total number of nodes which were added to the frontier
   * but never expended at the end of the search.
   */
  public int openNodes();
}