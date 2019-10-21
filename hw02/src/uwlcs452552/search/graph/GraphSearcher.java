package uwlcs452552.search.graph;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import uwlcs452552.search.SearchFailureException;

/**
 *  Topmost class encapsulating graph search.  The {@link
 *  uwlcs452552.search.graph.GraphSearcher#search search} method
 *  implements the Graph-Search algorithm of Russell and Norvig (2nd
 *  ed., Figure 3.7, p. 77), with customizable behavior provided as
 *  constructor arguments.
 *
 * @param <State> Type representing elements of the search space.
 * @param <Node> Type representing nodes in the search tree.  Each
 * node typically contains a reference to a State element.
 * @param <Frontier> Type representing the (entire) search frontier
 * (open set).
 */
public class GraphSearcher<State,
                           Node extends SearchTreeNode<Node,State>,
                           Frontier extends FrontierStructure<Node>> {

  private final Supplier<GoalChecker<Node>> goalCheckerFactory;
  private final Supplier<? extends Frontier> frontierFactory;
  private final Function<Frontier,ExploredSet<Node>> exploredSetFactory;
  private final Function<State,Node> initializer;

  private long addedToFrontier=-1, expandedFromFrontier=-1;

  /**
   * The constructor parameters encode the particlar behavior which
   * distinguishes different search algorithms.
   *
   * @param goalCheckerFactory The {@link
   * java.util.function.Supplier#get get} method of this object must
   * return a predicate on tree nodes used to tell if they are goal
   * nodes.
   *
   * @param frontierFactory The {@link java.util.function.Supplier#get
   * get} method of this object returns a new, empty Frontier
   * instance.
   *
   * @param exploredSetFactory Structure used to manage adding
   * elements to the frontier, in particular for avoiing duplication.
   *
   * @param initializer Creates an initial tree node from a search
   * space element.
   */
  public GraphSearcher(Supplier<GoalChecker<Node>> goalCheckerFactory,
                       Supplier<? extends Frontier> frontierFactory,
                       Function<Frontier,ExploredSet<Node>> exploredSetFactory,
                       Function<State,Node> initializer) {
    this.goalCheckerFactory = goalCheckerFactory;
    this.frontierFactory = frontierFactory;
    this.exploredSetFactory = exploredSetFactory;
    this.initializer = initializer;
  }

  /**
   *  Executes a search beginning from a particular search space
   *  element.
   *
   * @param initial The starting element
   * @return The tree node corresponding to a goal element of the
   * search space which is returned from this search
   * @throws SearchFailureException When the search does not lead to a
   * goal state
   */
  public Node search(State initial) throws SearchFailureException {

    // Initialize the frontier and the root node of the search tree.
    final Frontier frontier = frontierFactory.get();
    final Node initialNode = initializer.apply(initial);
    if (getDebug()) { debugInitialNode(initialNode); }
    frontier.add(initialNode);
    this.addedToFrontier = 1;
    this.expandedFromFrontier = 0;

    // Create a new, empty existing set.  In this framework the
    // existing set also checks memberhsip in the frontier.
    final ExploredSet<Node> exploredSet = exploredSetFactory.apply(frontier);
    exploredSet.noteInitial(initialNode);

    // Initialize the manager for reacting to expanding a node
    // containing a goal state.
    final GoalChecker<Node> goalChecker = goalCheckerFactory.get();

    // While the frontier is not empty, choose a leaf node and remove
    // it from the frontier.
    while (!frontier.isEmpty()) {
      final Node node = frontier.pop();
      final State state = node.getState();
      if (getDebug()) { debugFrontierRemoval(node); }

      // If the node contains a goal state (the goalChecker decided to
      // tell us so), then return the result.
      if (goalChecker.test(node)) {
        if (getDebug()) { debugGoalFound(node); }
        return node;
      }

      // Add this node to the explored set, as we are exploring it
      // right now.
      exploredSet.noteExplored(node);

      // Expand the node, and add the resulting nodes to the
      // frontier if they are not already in either the frontier or
      // the explored set.
      this.expandedFromFrontier += 1;
      for(final Node childNode : node.expand()) {
        if (getDebug()) { debugExpansion(childNode); }

        if (exploredSet.shouldAddToFrontier(childNode)) {
          if (getDebug()) { debugFrontierAddition(childNode); }
          this.addedToFrontier += 1;
          frontier.add(childNode);
        } else {
          if (getDebug()) { debugFrontierNonaddition(childNode); }
        }
      }
    }

    // If the goalChecker has been hoarding or otherwise processing
    // goals, let it give a final answer --- otherwise it can throw
    // the exception.
    if (getDebug()) { debugFrontierExhausted(goalChecker); }
    return goalChecker.get();
  }


  /**
   *  Convenience method for when we care only about whether a
   *  solution exists, and not what it is.
   *
   * @param initial The starting element
   * @return <tt>true</tt> if {@link #search} would return a final
   * node with the same initial state, otherwise <tt>false</tt>
   */
  public boolean solvable(final State initial) {
    try {
      final Node result = search(initial);
      return true;
    } catch (SearchFailureException e) {
      return false;
    }
  }

  /**
   *  This method prints a debugging message about the initial tree
   *  node of a search.
   *
   * @param node The tree node in question.
   */
  public void debugInitialNode(Node node) {
    System.out.println("Initial node: " + node);
  }

  /**
   *  This method prints a debugging message when a tree node is
   *  removed from the frontier for expansion.
   *
   * @param node The tree node in question.
   */
  public void debugFrontierRemoval(Node node) {
    System.out.println("Popped node " + node);
  }

  /**
   *  This method prints a debugging message when a tree node is
   *  generated from another node extracted from the frontier for
   *  expansion.
   *
   * @param node The tree node in question.
   */
  public void debugExpansion(Node node) {
    System.out.println("- Expanded to " + node);
  }

  /**
   *  This method prints a debugging message when a tree node is added
   *  to the frontier.
   *
   * @param node The tree node in question.  The default message
   * printed in the version of this method defined in this class does
   * not actually use this argument, since the tree node is already
   * printed in the default version of the {@link #debugExpansion}
   * method.
   */
  public void debugFrontierAddition(Node node) {
    System.out.println("  - Added");
  }

  /**
   *  This method prints a debugging message when a tree node is
   *  <em>not</em> added to the frontier.
   *
   * @param node The tree node in question
   */
  public void debugFrontierNonaddition(Node node) {
    System.out.println("  - Not added");
  }

  /**
   *  This method prints a debugging message when the frontier is
   *  emptied (which is usually an error situation).
   *
   * @param checker The {@link GoalChecker} instance used in this
   * search.  This instance may be storing expanded nodes (to avoid
   * revisiting them), and so may have additional information we wish
   * to view.  However the default implementation of this method in
   * this parent class does not use this argument.
   */
  public void debugFrontierExhausted(GoalChecker<Node> checker) {
    System.out.println("Frontier exhausted");
  }

  /**
   *
   */
  public void debugGoalFound(Node node) {
    System.out.println("- Node is goal");
  }

  /**
   *  Returns the number of frontier nodes which were added to the
   *  last search.
   * @return -1 if no search has been executed
   */
  public long getLastAddedToFrontier() {
    return addedToFrontier;
  }

  /**
   *  Returns the number of frontier nodes which were expanded in the
   *  last search.
   * @return -1 if no search has been executed
   */
  public long getLastExpandedFromFrontier() {
    return expandedFromFrontier;
  }

  private boolean debug = false;

  /**
   *  Controls whether execution of the {@link #search} method should
   *  print debugging messages.
   */
  public void setDebug(boolean debug) { this.debug = debug; }

  /**
   *  Returns whether execution of the {@link #search} method is
   *  printing debugging messages.
   */
  public boolean getDebug() { return debug; }
}
