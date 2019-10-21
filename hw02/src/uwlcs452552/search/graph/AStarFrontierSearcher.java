package uwlcs452552.search.graph;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *  Extension of {@link PriorityQueueSearcher} with A*'s
 *  prioritization formula <i>f(n) = g(n)+h(n)</i>, still leaving the
 *  exact structure of the frontier as a configurable option.
 *
 * @param <State> Type representing elements of the search space.
 * @param <Node> Type representing nodes in the search tree.  Each
 * node typically contains a reference to a State element.
 * @param <Frontier> Type representing the (entire) search frontier
 * (open set).
 */
public class AStarFrontierSearcher
    <State,
         Node extends SearchTreeNode<Node,State> & KnowsOwnCost,
         Frontier extends Frontiers.PriorityQueue<Node>>
    extends PriorityQueueSearcher<State, Node, Frontier> {

  private final Function<Node,Double> heuristic;

  /**
   * Constructor for this class which does not maintain an explored
   * set.
   *
   * @param goalTest A boolean-returning function checking whether a
   * tree node contains a goal state.
   *
   * @param heuristic Heuristic function for this search application.
   *
   * @param frontierMetafactory This function maps a {@link
   * Comparator} for tree nodes to a {@link
   * java.util.function.Supplier Supplier} of new, empty Frontier
   * instances.
   *
   * @param initializer Creates an initial tree node from a search
   * space element.  Passed as-is to the primary constructor for this
   * class, and thence to the {@linkplain GraphSearcher parent}
   * constructor.
   */
  public AStarFrontierSearcher
      (final Predicate<Node> goalTest,
       final Function<Node,Double> heuristic,
       final Function<Comparator<Node>,
                      Supplier<? extends Frontier>> frontierMetafactory,
       final Function<State,Node> initializer) {
    this(goalTest, heuristic, frontierMetafactory,
         ExploredSets.doNotTrack(), initializer);
  }

  /**
   *  Primary constructor for this class; other constructor relay to
   *  this one.  This constructor encodes A*'s <i>f(n) = g(n)+h(n)</i>
   *  formula into the {@link Comparator} behind the underlying
   *  priority queue.
   *
   * @param goalTest A boolean-returning function checking whether a
   * tree node contains a goal state.
   *
   * @param heuristic Heuristic function for this search application.
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
  public AStarFrontierSearcher
      (final Predicate<Node> goalTest,
       final Function<Node,Double> heuristic,
       final Function<Comparator<Node>,
                      Supplier<? extends Frontier>> frontierMetafactory,
       final Function<Frontier, ExploredSet<Node>> exploredSetFactory,
       final Function<State,Node> initializer) {
    super(() -> GoalCheckers.firstGoal(goalTest),
          new Comparator<Node>() {
            @Override public int compare(Node n1, Node n2) {
              final double diff = (n2.getCost() + heuristic.apply(n2)
                                   - n1.getCost() - heuristic.apply(n1));
              return diff<0 ? 1 : diff>0 ? -1 : 0;
            }
          },
          frontierMetafactory, exploredSetFactory, initializer);
    this.heuristic = heuristic;
  }

  /** {@inheritDoc} */
  @Override public void debugFrontierRemoval(Node node) {
    System.out.println("Popped node " + node
                       + " h=" + heuristic.apply(node));
  }

  /** {@inheritDoc} */
  @Override public void debugFrontierAddition(Node node) {
    System.out.println("- Adding node " + node
                       + " h=" + heuristic.apply(node));
  }

  /**
   *  A specialization of {@link AStarFrontierSearcher} to use a
   *  minimal implementation of unrelated search tree nodes (with a
   *  state and accumulated cost only), with the frontier
   *  implementation still exposed as a type parameter.
   *
   * @param <State> Type representing elements of the search space.
   * @param <Frontier> Type representing the (entire) search frontier
   * (open set).
   */
  public static class SimpleNodes
      <State, Frontier extends
              Frontiers.PriorityQueue<Nodes.SimpleTreeCostNode<State>>>
      extends AStarFrontierSearcher
              <State, Nodes.SimpleTreeCostNode<State>, Frontier> {
    /**
     * Constructor for this class which does not maintain an explored
     * set.
     *
     * @param stateTest A boolean-returning function checking whether
     * a state space element is a goal state.
     *
     * @param heuristic Heuristic function for this search application.
     *
     * @param frontierMetafactory This function maps a {@link
     * Comparator} for tree nodes to a {@link
     * java.util.function.Supplier Supplier} of new, empty Frontier
     * instances.
     *
     * @param expander Generates the successor states from some state,
     * each associated with a cost.
     */
    public SimpleNodes
        (final Predicate<State> stateTest,
         final Function<State,Double> heuristic,
         final Function<Comparator<Nodes.SimpleTreeCostNode<State>>,
                        Supplier<? extends Frontier>> frontierMetafactory,
         final Function<State,Iterable<Nodes.CostAndStep<State>>> expander) {
      super(GoalCheckers.liftPredicate(stateTest),
            Nodes.liftHeuristic(heuristic),
            frontierMetafactory,
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
     * @param frontierMetafactory This function maps a {@link
     * Comparator} for tree nodes to a {@link
     * java.util.function.Supplier Supplier} of new, empty Frontier
     * instances.
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
         final Function<Comparator<Nodes.SimpleTreeCostNode<State>>,
                        Supplier<? extends Frontier>> frontierMetafactory,
         final Function<State,Object> hashArtifactBuilder,
         final Function<State,Iterable<Nodes.CostAndStep<State>>> expander) {
      super(GoalCheckers.liftPredicate(stateTest),
            Nodes.liftHeuristic(heuristic),
            frontierMetafactory,
            ExploredSets.trackGeneratedByArtifactHashSet
                ((n) -> hashArtifactBuilder.apply(n.getState())),
            Nodes.SimpleTreeCostNode.initializer(expander));
    }
  }

  /**
   *  A specialization of {@link AStarFrontierSearcher} to use a
   *  minimal implementation of hierarchical search tree nodes (with a
   *  state, accumulated cost, and pointer to a parent tree node),
   *  with the frontier implementation still exposed as a type
   *  parameter.
   *
   * @param <State> Type representing elements of the search space.
   * @param <Frontier> Type representing the (entire) search frontier
   * (open set).
   */
  public static class PathNodes
      <State, Frontier extends
              Frontiers.PriorityQueue<Nodes.SimpleTreePathCostNode<State>>>
      extends AStarFrontierSearcher
              <State, Nodes.SimpleTreePathCostNode<State>, Frontier> {
    /**
     * Constructor for this class which does not maintain an explored
     * set.
     *
     * @param stateTest A boolean-returning function checking whether
     * a state space element is a goal state.
     *
     * @param heuristic Heuristic function for this search application.
     *
     * @param frontierMetafactory This function maps a {@link
     * Comparator} for tree nodes to a {@link
     * java.util.function.Supplier Supplier} of new, empty Frontier
     * instances.
     *
     * @param expander Generates the successor states from some state,
     * each associated with a cost.
     */
    public PathNodes
        (final Predicate<State> stateTest,
         final Function<State,Double> heuristic,
         final Function<Comparator<Nodes.SimpleTreePathCostNode<State>>,
                        Supplier<? extends Frontier>>
             frontierMetafactory,
         final Function<State,Iterable<Nodes.CostAndStep<State>>> expander) {
      super(GoalCheckers.liftPredicate(stateTest),
            Nodes.liftHeuristic(heuristic),
            frontierMetafactory,
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
     * @param frontierMetafactory This function maps a {@link
     * Comparator} for tree nodes to a {@link
     * java.util.function.Supplier Supplier} of new, empty Frontier
     * instances.
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
         final Function<Comparator<Nodes.SimpleTreePathCostNode<State>>,
                        Supplier<? extends Frontier>>
             frontierMetafactory,
         final Function<State,Object> hashArtifactBuilder,
         final Function<State,Iterable<Nodes.CostAndStep<State>>> expander) {
      super(GoalCheckers.liftPredicate(stateTest),
            Nodes.liftHeuristic(heuristic),
            frontierMetafactory,
            ExploredSets.trackGeneratedByArtifactHashSet
                ((n) -> hashArtifactBuilder.apply(n.getState())),
            Nodes.SimpleTreePathCostNode.initializer(expander));
    }
  }
}
