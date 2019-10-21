package uwlcs452552.h2;

import java.util.ArrayList;
import java.util.Iterator;
import uwlcs452552.search.graph.KnowsOwnCost;
import uwlcs452552.search.graph.SearchTreeNode;
import uwlcs452552.h2.model.BoardState;
import uwlcs452552.h2.model.Move;
import uwlcs452552.h2.model.PlacedCar;

/**
 *  Search tree node for building RushHour solution move sequences.
 */
public class BoardNode
    implements SearchTreeNode<BoardNode,BoardState>, KnowsOwnCost {
  private final double cost;
  private final BoardState board;
  private final Move move;
  private final BoardNode parent;

  /**
   * Convenience constructor for a root tree node.  The parent is
   * taken to be <tt>null</tt>, the last move is {@link Move#NONE
   * NONE}, and the sunk cost is zero.
   *  @param board {@link BoardState} associated with this tree node
   */
  public BoardNode(BoardState board) {
    this(0, board, Move.NONE, null);
  }

  /**
   * Primary constructor.
   *  @param cost  Sunk cost for reaching this state from the root
   *  @param board {@link BoardState} associated with this tree node
   *  @param move {@link Move} used to arrive in this state from the
   *  parent tree's state
   *  @param parent Parent node in the search tree
   */
  public BoardNode(double cost, BoardState board, Move move,
                   BoardNode parent) {
    this.cost = cost;
    this.move = move;
    this.board = board;
    this.parent = parent;
  }

  @Override public double getCost() { return cost; }
  @Override public BoardState getState() { return board; }
  public boolean hasGoalState() { return board.isGoalState(); }
  public Move getMove() { return move; }
  public BoardNode getParent() { return parent; }

  public ArrayList<Move> fillPath() {
    return fillPath(new ArrayList<Move>());
  }

  ArrayList<Move> fillPath(ArrayList<Move> moves) {
    if (parent != null) {
      parent.fillPath(moves);
      moves.add(getMove());
    }
    return moves;
  }

  public Iterable<BoardNode> expand() {
    return () -> new Iterator<BoardNode>() {
        final Iterator<Move> nextMoves = board.getValidMoves().iterator();
        @Override public boolean hasNext() { return nextMoves.hasNext(); }
        @Override public BoardNode next() {
          final Move nextMove = nextMoves.next();
          return new BoardNode(cost+1, nextMove.apply(board), nextMove,
                               BoardNode.this);
        }
      };
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder();

    if (hasGoalState()) {
      sb.append("Goal state\n");
    } else {
      sb.append("Non-goal state\n");
    }
    board.toString(sb);
    return sb.toString();
  }

  @Override public int hashCode() { return getState().hashCode(); }

  @Override public boolean equals(Object o) {
    if (o instanceof BoardNode) {
      BoardNode that = (BoardNode)o;
      return getState().equals(that.getState());
    } else {
      return false;
    }
  }
}

