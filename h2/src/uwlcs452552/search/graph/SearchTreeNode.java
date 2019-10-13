package uwlcs452552.search.graph;

public interface SearchTreeNode<Self extends SearchTreeNode<Self,State>,State> {

  public State getState();

  public Iterable<Self> expand();
}

