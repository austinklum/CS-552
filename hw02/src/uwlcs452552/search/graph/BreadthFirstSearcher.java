package uwlcs452552.search.graph;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Specialization of the generic {@link GraphSearcher} to use a queue
 * for its frontier, and thus erform breadth-first search.
 *
 * @param <State> Type representing elements of the search space.
 * @param <Node> Type representing nodes in the search tree.  Each
 * node typically contains a reference to a State element.
 */
public class BreadthFirstSearcher
    <State, Node extends SearchTreeNode<Node,State>>
    extends GraphSearcher<State, Node, Frontiers.Queue<Node>> {

  /**
   *  Constructor which uses a {@linkplain
   *  java.util.function.Predicate predicate} on goals for checking
   *  success of a tree node, and which defaults to a {@link
   *  java.util.HashSet HashSet} on state elements for detecting
   *  previously-explored nodes.
   *
   * @param stateChecker Success predicate on state elements.
   *
   * @param initializer Creates an initial tree node from a search
   * space element.
   */
  public BreadthFirstSearcher(Predicate<State> stateChecker,
                              Function<State,Node> initializer) {
    this(GoalCheckers.goalCheckerFactory(stateChecker), initializer);
  }

  /**
   *  Constructor which defaults to a {@link java.util.HashSet
   *  HashSet} on state elements for detecting previously-explored
   *  nodes.
   *
   * @param goalCheckerFactory The {@link
   * java.util.function.Supplier#get get} method of this object must
   * return a predicate on tree nodes used to tell if they are goal
   * nodes.
   *
   * @param initializer Creates an initial tree node from a search
   * space element.
   */
  public BreadthFirstSearcher(Supplier<GoalChecker<Node>> goalCheckerFactory,
                              Function<State,Node> initializer) {
    this(goalCheckerFactory, ExploredSets.trackStateByHashSet(),
         initializer);
  }

  /**
   *  Primary constructor for this class.  Passes its arguments to the
   *  {@linkplain GraphSearcher superclass} constructor, along with a
   *  factory for queue-based frontiers.  Other constructors for this
   *  class invoke this constructor.
   *
   * @param goalCheckerFactory The {@link
   * java.util.function.Supplier#get get} method of this object must
   * return a predicate on tree nodes used to tell if they are goal
   * nodes.
   *
   * @param exploredSetFactory Structure used to manage adding
   * elements to the frontier, in particular for avoiing duplication.
   *
   * @param initializer Creates an initial tree node from a search
   * space element.
   */
  public BreadthFirstSearcher
      (Supplier<GoalChecker<Node>> goalCheckerFactory,
       Function<Frontiers.Queue<Node>,ExploredSet<Node>> exploredSetFactory,
       Function<State,Node> initializer) {
    super(goalCheckerFactory, Frontiers.queueFactory(),
          exploredSetFactory, initializer);
  }

  public static <S> BreadthFirstSearcher<S, Nodes.SimpleTreeNode<S>>
      build(Predicate<S> stateChecker, Function<S,Iterable<S>> expander) {
    return new BreadthFirstSearcher<S, Nodes.SimpleTreeNode<S>>
        (GoalCheckers.goalCheckerFactory(stateChecker),
         Nodes.SimpleTreeNode.initializer(expander));
  }
}

