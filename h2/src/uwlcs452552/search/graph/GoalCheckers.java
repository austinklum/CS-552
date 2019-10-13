package uwlcs452552.search.graph;
import java.util.function.Predicate;
import java.util.function.Supplier;
import uwlcs452552.search.SearchFailureException;

public class GoalCheckers {

  public static <Node> GoalChecker<Node>
      firstGoal(final Predicate<Node> checker) {
    return new GoalChecker<Node>() {
      public Node get() throws SearchFailureException {
        throw new SearchFailureException();
      }
      public boolean test(Node n) {
        return checker.test(n);
      }
    };
  }

  public static <S, N extends SearchTreeNode<N,S>>
      Supplier<GoalChecker<N>> goalCheckerFactory(final Predicate<S> pred) {
    return () -> new GoalChecker<N>() {
        public boolean test(N n) { return pred.test(n.getState()); }
        public N get() throws SearchFailureException {
          throw new SearchFailureException();
        }
      };
  }

  public static <S, N extends SearchTreeNode<N,S>> Predicate<N>
      liftPredicate(final Predicate<S> pred) {
    return (n) -> pred.test(n.getState());
  }
}

