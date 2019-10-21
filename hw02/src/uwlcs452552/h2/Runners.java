package uwlcs452552.h2;
import java.util.ArrayList;
import uwlcs452552.search.SearchFailureException;
import uwlcs452552.h2.model.Boards;
import uwlcs452552.h2.model.BoardState;
import uwlcs452552.h2.model.Move;

/**
 *  Provides methods for running sample boards to classes implementing
 *  Rushhour solution search via the standard graph search hierarchy.
 */
public interface Runners {

  /**
   *  This method for finding a path from start to stop state much be
   *  present.
   */
  public BoardNode search(BoardState b) throws SearchFailureException;

  /**
   *  Capture the total number of nodes added to the frontier after a
   *  call to {@link #search}.
   */
  public long getLastAddedToFrontier();

  /**
   *  Capture the total number of nodes expanded from the frontier
   *  after a call to {@link #search}.
   */
  public long getLastExpandedFromFrontier();

  /**
   *  Expands one @{link BoardState}, and returns the sequence of
   *  moves in the solution.
   */
  default public ArrayList<Move> runSearch(BoardState board) {
    try {
      final ArrayList<Move>
          result = search(board).fillPath(new ArrayList<Move>());
      return result;
    } catch (SearchFailureException sfe) {
      return new ArrayList<Move>();
    }
  }

  /**
   *  Runs {@link #search} on all of the boards registered in the
   *  {@link uwlcs452552.h2.model.Boards#BOARDS Boards.BOARDS} sample
   *  list.  This method is useful to call from the <tt>main</tt>
   *  method of a fully-conrete A* or BFS searcher for Rushhour.
   */
  default public void runSampleBoards() {
    for(final String name : Boards.BOARDS.keySet()) {
      final BoardState board = Boards.BOARDS.get(name);
      final ArrayList<Move> solution = runSearch(board);

      System.out.printf
          ("%s: solution length %d, added %d nodes, expanded %d\n",
           name, solution.size(), getLastAddedToFrontier(),
           getLastExpandedFromFrontier());
      // System.out.println(" - " + Move.formatMoves(solution));

      // if (solution.size()>0) {
      //   System.out.print(" - Checking solution...");
      //   if (board.checkSolution(solution)) {
      //     System.out.println("correct");
      //   } else {
      //     System.out.println("INCORRECT");
      //   }
      // }
    }
  }
}
