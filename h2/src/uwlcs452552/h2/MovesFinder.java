package uwlcs452552.h2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;
import uwlcs452552.search.SearchFailureException;
import uwlcs452552.search.graph.AStarSearcher;
import uwlcs452552.search.graph.KnowsOwnCost;
import uwlcs452552.search.graph.SearchTreeNode;
import uwlcs452552.search.graph.ExploredSets;
import uwlcs452552.search.graph.Frontiers;
import uwlcs452552.h2.model.BoardState;
import uwlcs452552.h2.model.Move;
import uwlcs452552.h2.model.PlacedCar;
import static uwlcs452552.h2.model.Move.NONE;

public class MovesFinder extends AStarSearcher<BoardState,BoardNode>
    implements Runners {

  public MovesFinder(Function<BoardState,Double> heuristic) {
    super((BoardNode node) -> node.hasGoalState(),
          (BoardNode node) -> heuristic.apply(node.getState()),
          ExploredSets.trackGeneratedByArtifactHashSet
              ((node) -> node.getState().boardString()),
          (BoardState board) -> new BoardNode(board,NONE));
    // setDebug(true);
  }
}
