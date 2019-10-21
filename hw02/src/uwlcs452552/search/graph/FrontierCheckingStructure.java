package uwlcs452552.search.graph;

public interface FrontierCheckingStructure<Node>
    extends FrontierStructure<Node> {

  public boolean contains(Node n);
}
