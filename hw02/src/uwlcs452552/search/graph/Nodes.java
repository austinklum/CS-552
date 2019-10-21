
package uwlcs452552.search.graph;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Predicate;
import uwlcs452552.search.SearchFailureException;

public final class Nodes {

  public static class CostAndStep<S> {
    private final double cost;
    private final S state;
    public CostAndStep(double cost, S state) {
      this.cost = cost;
      this.state = state;
    }
    public double getCost() { return cost; }
    public S getState() { return state; }
  }

  public static <S, N extends SearchTreeNode<N,S>> Function<N,Double>
      liftHeuristic(Function<S,Double> hs) {
    return (n) -> hs.apply(n.getState());
  }

  // /////////////////////////////////////////////////////////////////
  // Just a simple wrapper for the state.  No cost, no history

  static abstract class
      SimpleCoreTreeNode<Self extends SimpleCoreTreeNode<Self, Exp, S>, Exp, S>
      implements SearchTreeNode<Self, S> {
    final Function<S,Iterable<Exp>> expander;
    private final S state;
    SimpleCoreTreeNode(Function<S,Iterable<Exp>> expander, S state) {
      this.expander = expander;
      this.state = state;
    }

    public S getState() { return state; }

    @Override public String toString() {
      return "[" + getState().toString() + "]";
    }
  };

  public static class SimpleTreeNode<S>
      extends SimpleCoreTreeNode<SimpleTreeNode<S>,S,S> {
    public SimpleTreeNode(Function<S,Iterable<S>> expander, S state) {
      super(expander,state);
    }

    public Iterable<SimpleTreeNode<S>> expand() {
      return () -> new Iterator<SimpleTreeNode<S>>() {
          final SimpleTreeNode<S> node = SimpleTreeNode.this;
          final Iterator<S>
              dests = node.expander.apply(node.getState()).iterator();
          @Override public boolean hasNext() { return dests.hasNext(); }
          @Override public SimpleTreeNode<S> next() {
            final S dest = dests.next();
            return new SimpleTreeNode<S>(expander,dest);
          }
        };
    }

    public static final <S> Function<S,SimpleTreeNode<S>>
        initializer(final Function<S,Iterable<S>> expander) {
      return (final S state) -> new SimpleTreeNode<S>(expander, state);
    }
  }

  // /////////////////////////////////////////////////////////////////
  // The state and the cost.

  static abstract class
      SimpleCoreTreeCostNode<Self extends
                                    SimpleCoreTreeCostNode<Self, Exp, S>,
                             Exp extends CostAndStep<S>, S>
      extends SimpleCoreTreeNode<Self, Exp, S>
      implements KnowsOwnCost {
    private final double cost;
    SimpleCoreTreeCostNode(Function<S,Iterable<Exp>> expander,
                           double cost, S state) {
      super(expander, state);
      this.cost = cost;
    }

    public double getCost() { return cost; }

    @Override public String toString() {
      return "[" + getState().toString() + "@" + getCost() + "]";
    }
  };

  public static class SimpleTreeCostNode<S>
      extends SimpleCoreTreeCostNode<SimpleTreeCostNode<S>,CostAndStep<S>,S> {
    public SimpleTreeCostNode(Function<S,Iterable<CostAndStep<S>>> expander,
                              double cost, S state) {
      super(expander,cost,state);
    }
    public Iterable<SimpleTreeCostNode<S>> expand() {
      return () -> new Iterator<SimpleTreeCostNode<S>>() {
          final SimpleTreeCostNode<S> node = SimpleTreeCostNode.this;
          final Iterator<CostAndStep<S>>
              dests = node.expander.apply(node.getState()).iterator();
          @Override public boolean hasNext() { return dests.hasNext(); }
          @Override public SimpleTreeCostNode<S> next() {
            final CostAndStep<S> cs = dests.next();
            final S dest = cs.getState();
            final double cost = cs.getCost();
            return new SimpleTreeCostNode<S>
                (expander, SimpleTreeCostNode.this.getCost()+cost, dest);
          }
        };
    }

    public static final <S> Function<S, SimpleTreeCostNode<S>>
        initializer(final Function<S,Iterable<CostAndStep<S>>> expander) {
      return (final S state)
          -> new SimpleTreeCostNode<S>(expander, 0.0, state);
    }
  }

  // /////////////////////////////////////////////////////////////////
  // The state plus a pointer to the parent node.

  static abstract class
      SimpleCoreTreePathNode<Self extends
                                    SimpleCoreTreePathNode<Self,Exp,S>, Exp, S>
      extends SimpleCoreTreeNode<Self, Exp, S>
      implements SearchTreePathNode<Self, S> {
    private final Self parent;
    SimpleCoreTreePathNode(Function<S,Iterable<Exp>> expander, S state) {
      this(expander, null, state);
    }
    SimpleCoreTreePathNode(Self parent, S state) {
      this(parent.expander, parent, state);
    }
    SimpleCoreTreePathNode(Function<S,Iterable<Exp>> expander,
                       Self parent, S state) {
      super(expander,state);
      this.parent = parent;
    }

    public Self getParent() { return parent; }
    @Override public String toString() {
      return "[" + pathToString() + "]";
    }

  };

  public static class SimpleTreePathNode<S>
      extends SimpleCoreTreePathNode<SimpleTreePathNode<S>,S, S> {
    public SimpleTreePathNode(Function<S,Iterable<S>> expander, S state) {
      super(expander,null,state);
    }
    public SimpleTreePathNode(SimpleTreePathNode<S> parent, S state) {
      super(parent.expander,parent,state);
    }
    public Iterable<SimpleTreePathNode<S>> expand() {
      return () -> new Iterator<SimpleTreePathNode<S>>() {
          final SimpleTreePathNode<S> node = SimpleTreePathNode.this;
          final Iterator<S>
              dests = node.expander.apply(node.getState()).iterator();
          @Override public boolean hasNext() { return dests.hasNext(); }
          @Override public SimpleTreePathNode<S> next() {
            final S dest = dests.next();
            return new SimpleTreePathNode<S>(node,dest);
          }
        };
    }

    public static final <S> Function<S, SimpleTreePathNode<S>>
        initializer(final Function<S,Iterable<S>> expander) {
      return (final S state) -> new SimpleTreePathNode<S>(expander, state);
    }
  }

  // /////////////////////////////////////////////////////////////////
  // The state, the cost, and a pointer to the parent node.

  static abstract class
      SimpleCoreTreePathCostNode<Self extends
                                        SimpleCoreTreePathCostNode<Self,Exp,S>,
                                        Exp extends CostAndStep<S>, S>
      extends SimpleCoreTreePathNode<Self, Exp, S>
      implements KnowsOwnCost {
    private final double cost;
    SimpleCoreTreePathCostNode(Function<S,Iterable<Exp>> expander,
                               double cost, S state) {
      this(expander, null, cost, state);
    }
    SimpleCoreTreePathCostNode(Self parent, double cost, S state) {
      this(parent.expander, parent, cost, state);
    }
    SimpleCoreTreePathCostNode(Function<S,Iterable<Exp>> expander,
                               Self parent, double cost, S state) {
      super(expander,parent,state);
      this.cost = cost;
    }

    public double getCost() { return cost; }

    @Override public String toString() {
      return "[" + pathToString() + "@" + getCost() + "]";
    }
  };

  public static class SimpleTreePathCostNode<S>
      extends SimpleCoreTreePathCostNode<SimpleTreePathCostNode<S>,
                                         CostAndStep<S>,
                                         S> {
    public SimpleTreePathCostNode
        (Function<S,Iterable<CostAndStep<S>>> expander, double cost, S state) {
      super(expander,null,cost,state);
    }
    public SimpleTreePathCostNode(SimpleTreePathCostNode<S> parent,
                                  double cost, S state) {
      super(parent.expander,parent,cost,state);
    }
    public Iterable<SimpleTreePathCostNode<S>> expand() {
      return () -> new Iterator<SimpleTreePathCostNode<S>>() {
          final SimpleTreePathCostNode<S> node = SimpleTreePathCostNode.this;
          final Iterator<CostAndStep<S>>
              dests = node.expander.apply(node.getState()).iterator();
          @Override public boolean hasNext() { return dests.hasNext(); }
          @Override public SimpleTreePathCostNode<S> next() {
            final CostAndStep<S> cs = dests.next();
            final S dest = cs.getState();
            final double cost = cs.getCost();
            return new SimpleTreePathCostNode<S>
                (node, SimpleTreePathCostNode.this.getCost()+cost, dest);
          }
        };
    }

    public static final <S> Function<S, SimpleTreePathCostNode<S>>
        initializer(final Function<S,Iterable<CostAndStep<S>>> expander) {
      return (final S state)
          -> new SimpleTreePathCostNode<S>(expander, 0.0, state);
    }
  }
}
