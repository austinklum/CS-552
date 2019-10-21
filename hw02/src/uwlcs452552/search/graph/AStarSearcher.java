package uwlcs452552.search.graph;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *  Extension of {@link AStarFrontierSearcher} to fix the frontier
 *  structure with a minimal priority queue implementation.
 *
 * @param <State> Type representing elements of the search space.
 * @param <Node> Type representing nodes in the search tree.  Each
 * node typically contains a reference to a State element.
 */
public class AStarSearcher
    <State, Node extends SearchTreeNode<Node,State> & KnowsOwnCost>
    extends AStarFrontierSearcher<State, Node, Frontiers.PriorityQueue<Node>> {

  /**
   * Constructor for this class which does not maintain an explored
   * set.
   *
   * @param goalTest A boolean-returning function checking whether a
   * tree node contains a goal state.
   *
   * @param heuristic Heuristic function for this search application.
   *
   * @param initializer Creates an initial tree node from a search
   * space element.  Passed as-is to the primary constructor for this
   * class, and thence to superclasses.
   */
  public AStarSearcher
      (final Predicate<Node> goalTest,
       final Function<Node,Double> heuristic,
       final Function<State,Node> initializer) {
    super(goalTest, heuristic,
          (cmp) -> Frontiers.priorityQueueFactory(cmp),
          initializer);
  }

  /**
   *  Primary constructor for this class; other constructor relay to
   *  this one.  This constructor provides the {@linkplain
   *  Frontiers.PriorityQueue basic priority queue implementation} of
   *  the frontier to the superclass constructor.
   *
   * @param goalTest A boolean-returning function checking whether a
   * tree node contains a goal state.
   *
   * @param heuristic Heuristic function for this search application.
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
  public AStarSearcher
      (final Predicate<Node> goalTest,
       final Function<Node,Double> heuristic,
       final Function<Frontiers.PriorityQueue<Node>,
                      ExploredSet<Node>> exploredSetFactory,
       final Function<State,Node> initializer) {
    super(goalTest, heuristic, (cmp) -> Frontiers.priorityQueueFactory(cmp),
          exploredSetFactory, initializer);
  }

  /**
   *  A specialization of {@link AStarSearcher} to use a minimal
   *  implementation of unrelated search tree nodes (with a state and
   *  accumulated cost only), with the frontier implementation still
   *  exposed as a type parameter.
   *
   * @param <State> Type representing elements of the search space.
   */
  public static class SimpleNodes<State>
      extends AStarSearcher<State, Nodes.SimpleTreeCostNode<State>> {
    /**
     * Constructor for this class which does not maintain an explored
     * set.
     *
     * @param stateTest A boolean-returning function checking whether
     * a state space element is a goal state.
     *
     * @param heuristic Heuristic function for this search application.
     *
     * @param expander Generates the successor states from some state,
     * each associated with a cost.
     */
    public SimpleNodes
        (final Predicate<State> stateTest,
         final Function<State,Double> heuristic,
         final Function<State,Iterable<Nodes.CostAndStep<State>>> expander) {
      super(GoalCheckers.liftPredicate(stateTest),
            Nodes.liftHeuristic(heuristic),
            Nodes.SimpleTreeCostNode.initializer(expander));
    }

    /**
     * Constructor for this class which maintains an explored
     * set using a hashing of the state representations.
     *
     * @param stateTest A boolean-returning function checking whether
     * a state space element is a goal state.
     *
     * @param heuristic Heuristic function for this search application.
     *
     * @param hashArtifactBuilder Generates a hashable object from a
     * state element.
     *
     * @param expander Generates the successor states from some state,
     * each associated with a cost.
     */
    public SimpleNodes
        (final Predicate<State> stateTest,
         final Function<State,Double> heuristic,
         final Function<State,Object> hashArtifactBuilder,
         final Function<State,Iterable<Nodes.CostAndStep<State>>> expander) {
      super(GoalCheckers.liftPredicate(stateTest),
            Nodes.liftHeuristic(heuristic),
            ExploredSets.trackGeneratedByArtifactHashSet
                ((n) -> hashArtifactBuilder.apply(n.getState())),
            Nodes.SimpleTreeCostNode.initializer(expander));
    }
  }

  /**
   *  A specialization of {@link AStarSearcher} to use a minimal
   *  implementation of hierarchical search tree nodes (with a state,
   *  accumulated cost, and pointer to a parent tree node), with the
   *  frontier implementation still exposed as a type parameter.
   *
   * @param <State> Type representing elements of the search space.
   */
  public static class PathNodes<State>
      extends AStarSearcher<State, Nodes.SimpleTreePathCostNode<State>> {
    /**
     * Constructor for this class which does not maintain an explored
     * set.
     *
     * @param stateTest A boolean-returning function checking whether
     * a state space element is a goal state.
     *
     * @param heuristic Heuristic function for this search application.
     *
     * @param expander Generates the successor states from some state,
     * each associated with a cost.
     */
    public PathNodes
        (final Predicate<State> stateTest,
         final Function<State,Double> heuristic,
         final Function<State,Iterable<Nodes.CostAndStep<State>>> expander) {
      super(GoalCheckers.liftPredicate(stateTest),
            Nodes.liftHeuristic(heuristic),
            Nodes.SimpleTreePathCostNode.initializer(expander));
    }

    /**
     * Constructor for this class which maintains an explored
     * set using a hashing of the state representations.
     *
     * @param stateTest A boolean-returning function checking whether
     * a state space element is a goal state.
     *
     * @param heuristic Heuristic function for this search application.
     *
     * @param hashArtifactBuilder Generates a hashable object from a
     * state element.
     *
     * @param expander Generates the successor states from some state,
     * each associated with a cost.
     */
    public PathNodes
        (final Predicate<State> stateTest,
         final Function<State,Double> heuristic,
         final Function<State,Object> hashArtifactBuilder,
         final Function<State,Iterable<Nodes.CostAndStep<State>>> expander) {
      super(GoalCheckers.liftPredicate(stateTest),
            Nodes.liftHeuristic(heuristic),
            ExploredSets.trackGeneratedByArtifactHashSet
                ((n) -> hashArtifactBuilder.apply(n.getState())),
            Nodes.SimpleTreePathCostNode.initializer(expander));
    }
  }
}

