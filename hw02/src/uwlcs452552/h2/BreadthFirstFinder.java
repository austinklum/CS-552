package uwlcs452552.h2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;
import uwlcs452552.search.SearchFailureException;
import uwlcs452552.search.graph.BreadthFirstSearcher;
import uwlcs452552.search.graph.KnowsOwnCost;
import uwlcs452552.search.graph.SearchTreeNode;
import uwlcs452552.search.graph.GoalCheckers;
import uwlcs452552.search.graph.ExploredSets;
import uwlcs452552.h2.model.BoardState;
import uwlcs452552.h2.model.Move;
import uwlcs452552.h2.model.PlacedCar;

/**
 *  Model solution finder for RushHour boards using breadth-first
 *  search.
 */
public class BreadthFirstFinder
    extends BreadthFirstSearcher<BoardState,BoardNode>
    implements Runners {

  public BreadthFirstFinder() {
    super(() -> GoalCheckers.firstGoal((BoardNode cn) -> cn.hasGoalState()),
          ExploredSets.trackGeneratedByArtifactHashSet((n) -> n.getState().boardString()),
          (BoardState board) -> new BoardNode(board));
    // setDebug(true);
  }

  /**
   *  Tests the given sample boards using BFS.
   * @see Runners#runSampleBoards()
   */
  public static void main(String[] argv) {
    new BreadthFirstFinder().runSampleBoards();
  }

  @Override public void debugFrontierRemoval(BoardNode node) {
    super.debugFrontierRemoval(node);
    System.out.print(" - Available moves: ");
    String sep = "";
    String fin = "none";
    for(final Move move : node.getState().getValidMoves()) {
      System.out.print(sep);
      System.out.print(move.toString());
      sep = ", ";
      fin = "";
    }
    System.out.println(fin);
  }
}
