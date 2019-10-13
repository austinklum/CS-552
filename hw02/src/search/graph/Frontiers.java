package uwlcs452552.search.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class Frontiers {

  public static class PriorityQueue<Node> implements FrontierStructure<Node> {

    protected final java.util.PriorityQueue<Node> queue;

    public PriorityQueue(Comparator<Node> prioritizer) {
      this.queue = new java.util.PriorityQueue<Node>(prioritizer);
    }

    @Override public void add(Node n) {
      queue.add(n);
    }

    @Override public boolean isEmpty() {
      return queue.isEmpty();
    }

    @Override public Node pop() {
      final Node result = queue.poll();
      if (result == null) {
        throw new FrontierEmptyException();
      }
      return result;
    }
  }

  public static <Node> Supplier<PriorityQueue<Node>>
      priorityQueueFactory(final Comparator<Node> prioritizer) {
    return new Supplier<PriorityQueue<Node>>() {
      public PriorityQueue<Node> get() {
        return new PriorityQueue<Node>(prioritizer);
      }
    };
  }

  // = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

  public static class StateKeyedPriorityQueue<S, N extends SearchTreeNode<N,S>>
      extends PriorityQueue<N> {

    private final HashMap<S,N> bestPath = new HashMap<>();
    private final Comparator<N> prioritizer;

    public StateKeyedPriorityQueue(Comparator<N> prioritizer) {
      super(prioritizer);
      this.prioritizer = prioritizer;
    }

    @Override public void add(N node) {
      final S state = node.getState();
      if (bestPath.containsKey(state)) {
        final N previousBest = bestPath.get(state);
        if (prioritizer.compare(previousBest,node)>0) {
          queue.remove(previousBest);
          bestPath.put(state, node);
          super.add(node);
        }
      } else {
        bestPath.put(state, node);
        super.add(node);
      }
    }

  }

  public static <State, Node extends SearchTreeNode<Node,State>>
      Supplier<StateKeyedPriorityQueue<State,Node>>
      stateKeyedPriorityQueueFactory(final Comparator<Node> prioritizer) {
    return new Supplier<StateKeyedPriorityQueue<State,Node>>() {
      public StateKeyedPriorityQueue<State,Node> get() {
        return new StateKeyedPriorityQueue<State,Node>(prioritizer);
      }
    };
  }

  // =================================================================

  public static class Queue<Node> implements FrontierCheckingStructure<Node> {

    protected final java.util.LinkedList<Node>
        queue = new java.util.LinkedList<Node>();

    @Override public void add(Node n) {
      queue.offer(n);
    }

    @Override public boolean isEmpty() {
      return queue.isEmpty();
    }

    @Override public Node pop() {
      try {
        return queue.remove();
      } catch (NoSuchElementException cause) {
        throw new FrontierEmptyException(cause);
      }
    }

    @Override public boolean contains(Node n) {
      return queue.contains(n);
    }
  }

  public static <Node> Supplier<Queue<Node>> queueFactory() {
    return new Supplier<Queue<Node>>() {
      public Queue<Node> get() {
        return new Queue<Node>();
      }
    };
  }

}
