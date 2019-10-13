package uwlcs452552.search.graph;
import java.util.HashSet;
import java.util.function.Function;

public class ExploredSets {

  public static <F,N> Function<F,ExploredSet<N>> doNotTrack() {
    return new Function<F,ExploredSet<N>>() {
      public ExploredSet<N> apply(F frontier) {
        return new ExploredSet<N>() {
          @Override public void noteExplored(N n) { }
          @Override public void noteInitial(N n) { }
          @Override public boolean shouldAddToFrontier(N n) { return true; }
        };
      }
    };
  }

  public static <F,S,N extends SearchTreeNode<N,S>>
      Function<F,ExploredSet<N>> trackStateByHashSet() {
    return trackGeneratedByArtifactHashSet((x) -> x.getState());
  }

  public static <F,N,A> Function<F,ExploredSet<N>>
      trackGeneratedByArtifactHashSet(final Function<N,A> artifactBuilder) {
    return (F frontier) -> new ExploredSet<N>() {
        private final HashSet<A> tracker = new HashSet<A>();
        @Override public void noteExplored(N n) { }
        @Override public void noteInitial(N n) {
          final A artifact = artifactBuilder.apply(n);
          tracker.add(artifact);
        }
        @Override public boolean shouldAddToFrontier(N n) {
          final A artifact = artifactBuilder.apply(n);
          final boolean result = !tracker.contains(artifact);
          if (result) { tracker.add(artifact); }
          return result;
        }
      };
  }
}

