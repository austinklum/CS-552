package uwlcs452552.search.graph;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Specialization of the generic {@link GraphSearcher} to use a
 * priority queue structure for its frontier.
 *
 * @param <State> Type representing elements of the search space.
 * @param <Node> Type representing nodes in the search tree.  Each
 * node typically contains a reference to a State element.
 * @param <Frontier> Type representing the (entire) search frontier
 * (open set).
 */
public class
    PriorityQueueSearcher<State,
                          Node extends SearchTreeNode<Node,State>,
                          Frontier extends Frontiers.PriorityQueue<Node>>
    extends GraphSearcher<State, Node, Frontier> {

  /**
   *  This constructor allows the comparison function to be specified
   *  separately from the frontier-creation process.
   *
   * @param goalCheckerFactory The {@link
   * java.util.function.Supplier#get get} method of this object must
   * return a predicate on tree nodes used to tell if they are goal
   * nodes.  Passed as-is to the primary constructor for this class,
   * and thence to the {@linkplain GraphSearcher parent} constructor.
   *
   * @param prioritizer The method by which the priority queue ranks
   * tree nodes.  In Java's {@link java.util.PriorityQueue
   * PriorityQueue} implementation, the next element to be removed is
   * the one ranked <em>least</em> by the {@link
   * java.util.Comparator}.
   *
   * @param frontierMetafactory This function maps a {@link
   * Comparator} for tree nodes to a {@link
   * java.util.function.Supplier Supplier} of new, empty Frontier
   * instances.
   *
   * @param exploredSetFactory Structure used to manage adding
   * elements to the frontier, in particular for avoiing duplication.
   * Passed as-is to the primary constructor for this class, and
   * thence to the {@linkplain GraphSearcher parent} constructor.
   *
   * @param initializer Creates an initial tree node from a search
   * space element.  Passed as-is to the primary constructor for this
   * class, and thence to the {@linkplain GraphSearcher parent}
   * constructor.
   */
  public PriorityQueueSearcher
      (Supplier<GoalChecker<Node>> goalCheckerFactory,
       Comparator<Node> prioritizer,
       Function<Comparator<Node>, Supplier<? extends Frontier>>
           frontierMetafactory,
       Function<Frontier,ExploredSet<Node>> exploredSetFactory,
       Function<State,Node> initializer) {
    this(goalCheckerFactory, frontierMetafactory.apply(prioritizer),
         exploredSetFactory, initializer);
  }

  /**
   *  Primary constructor for this class.  Since the use of a priority
   *  queue is specified by the bounds on <tt>Frontier</tt>, this
   *  constructor simply passes its arguments to the {@linkplain
   *  GraphSearcher superclass} constructor.  Other constructors for
   *  this class invoke this constructor.
   *
   * @param goalCheckerFactory The {@link
   * java.util.function.Supplier#get get} method of this object must
   * return a predicate on tree nodes used to tell if they are goal
   * nodes.
   *
   * @param frontierFactory The {@link java.util.function.Supplier#get
   * get} method of this object returns a new, empty Frontier
   * instance.  Note that the type bound on <tt>Factory</tt> requires
   * that the generated objects conform to {@link
   * Frontiers.PriorityQueue Frontiers.PriorityQueue<Node>}.
   *
   * @param exploredSetFactory Structure used to manage adding
   * elements to the frontier, in particular for avoiing duplication.
   *
   * @param initializer Creates an initial tree node from a search
   * space element.
   */
  public PriorityQueueSearcher
      (Supplier<GoalChecker<Node>> goalCheckerFactory,
       Supplier<? extends Frontier> frontierFactory,
       Function<Frontier,ExploredSet<Node>> exploredSetFactory,
       Function<State,Node> initializer) {
    super(goalCheckerFactory, frontierFactory,
          exploredSetFactory, initializer);
  }

}

