package uwlcs452552.search.graph;
import java.util.ArrayList;

public interface SearchTreePathNode<This extends SearchTreePathNode<This,S>,S>
    extends SearchTreeNode<This,S> {

  public This getParent();

  public default ArrayList<S> statePath() {
    return statePath(new ArrayList<S>());
  }

  public default ArrayList<S> statePath(ArrayList<S> states) {
    final This parent = getParent();
    if (parent != null) {
      parent.statePath(states);
    }
    states.add(getState());
    return states;
  }

  public default String pathToString() {
    final This parent = getParent();
    if (parent == null) {
      return getState().toString();
    } else {
      return parent.pathToString() + " >> " + getState().toString();
    }
  }
}

