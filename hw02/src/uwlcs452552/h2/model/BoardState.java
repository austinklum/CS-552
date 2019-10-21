
package uwlcs452552.h2.model;
import java.util.Arrays;
import java.util.ArrayList;
import static uwlcs452552.h2.model.Move.Direction.*;

/**
 *  Model of one state of the Rushhour board.
 */
public class BoardState {

  /**
   *  The default size of the board, for when we do not specify it
   *  otherwise.
   */
  public static final int DEFAULT_BOARD_SIZE = 6;
  /**
   *  The default row one which the family car should live.
   */
  public static final int DEFAULT_OUR_ROW = 2;
  /**
   *  The default length of the family car.
   */
  public static final int DEFAULT_OUR_DEFAULT_LENGTH = 2;

  private final PlacedCar[] cars;
  private final int[][] filled;
  private final int boardSize;

  /**
   *  Constructor given a particular placement of cars on the board.
   */
  BoardState(PlacedCar[] cars) {
    this(cars, DEFAULT_BOARD_SIZE);
  }

  /**
   *  Constructor given a particular placement of cars on the board
   *  and board size.
   */
  BoardState(PlacedCar[] cars, int boardSize) {
    this.cars = Arrays.copyOf(cars, cars.length);
    this.boardSize = boardSize;
    this.filled = new int[boardSize][boardSize];

    // Initialize the filled array --- the length of the cars stands
    // for empty.
    for(int i=0; i<boardSize; i++) {
      for(int j=0; j<boardSize; j++) {
        filled[i][j] = cars.length;
      }
    }

    // Now add the ID of each car in the spaces they fill.
    for(final PlacedCar car : cars) {
      final int row = car.getRow(), col = car.getCol();
      for(int i=0; i<car.getLength(); i++) {
        if (car.isVertical()) {
          filled[row+i][col] = car.getId();
        } else {
          filled[row][col+i] = car.getId();
        }
      }
    }
  }

  /**
   *  Checks whether this state solves the puzzle by allowing the
   *  family car to escape.
   */
  public boolean isGoalState() {
    for(final PlacedCar car : cars) {
      if (car.isTargetCar()) { return car.atGoal(); }
    }
    return false;
  }

  /**
   *  Returns the size of this (square) board.
   */
  public int getBoardSize() { return boardSize; }
  /**
   *  Returns the number of cars placed on the board.
   */
  public int placed() { return cars.length; }
  /**
   *  Returns the placement of a particular car.
   *
   * @param i The index of the car, from 0 up to but not including
   * {@link #placed}
   * @return The placement record for that car index
   */
  public PlacedCar placement(int i) { return cars[i]; }
  /**
   *  Returns whether the board has a filled space at a particular row
   *  or column
   *
   * @param row The row number of the position.  Valid values are from
   * 0 up to (but including) {@link #getBoardSize}.
   *
   * @param col The column number of the position.  Valid values are
   * from 0 up to (but including) {@link #getBoardSize}.
   */
  public boolean filledAt(int row, int col) {
    return filled[row][col] < cars.length;
  }
  /**
   *  Returns a container from which the valid moves at this position
   *  may be iterated.
   */
  public Iterable<Move> getValidMoves() {
    final ArrayList<Move> validMoves = new ArrayList<>();

    for(final PlacedCar car : cars) {
      final int row = car.getRow(), col = car.getCol();
      if (car.isVertical()) {
        final int
            before = car.getRow()-1,
            after = car.getRow()+car.getLength();
        if (0<=before && !filledAt(before, col)) {
          validMoves.add(new Move(UP, car));
        }
        if (after<boardSize && !filledAt(after, col)) {
          validMoves.add(new Move(DOWN, car));
        }
      } else {
        final int
            before = car.getCol()-1,
            after = car.getCol()+car.getLength();
        if (0<=before && !filledAt(row, before)) {
          validMoves.add(new Move(LEFT, car));
        }
        if (after<boardSize && !filledAt(row, after)) {
          validMoves.add(new Move(RIGHT, car));
        }
      }
    }

    return validMoves;
  }

  /**
   *  Check whether a particular series of moves is a correct solution
   *  from this position.
   *
   * @param moves The sequence of {@link Move}s to check
   *
   * @return <tt>true</tt> for a correct series of moves.  There is no
   * distinction between a series of moves which leads to a
   * non-solution, and a sequence containing an illegal move, in this
   * method result; the body of this method catches {@link
   * MoveException}s.  Of course, other exceptions will propagate out.
   */
  public boolean checkSolution(final ArrayList<Move> moves) {
    try {
      BoardState progress = this;
      System.out.println();
      System.out.println(progress);
      for(final Move move : moves) {
        System.out.println(move);
        final BoardState newProgress = move.apply(progress);
        System.out.println(newProgress);
        progress = newProgress;
      }
      return progress.isGoalState();
    } catch (MoveException me) {
      System.out.println(me);
      return false;
    }
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder();
    toString(sb, "    ");
    return sb.toString();
  }

  /**
   *  Returns the string representation, with a given indentation.
   */
  public String toString(final String indentation) {
    final StringBuilder sb = new StringBuilder();
    toString(sb, indentation);
    return sb.toString();
  }

  /**
   *  Helper method for {@link #toString()} using a threaded {@link
   *  StringBuilder}.  Just relays to the all-parameters call.
   */
  public void toString(final StringBuilder sb) {
    toString(sb, "    ");
  }

  /**
   *  Full helper method for {@link #toString()}.  Since output goes
   *  to a {@link StringBuilder}, there is no need for a result.
   *
   * @param sb The {@link StringBuilder} to which output should be
   * written.
   *
   * @param ind The current indentation
   */
  public void toString(final StringBuilder sb, final String ind) {
    String sep = "";
    for(final int[] row : filled) {
      sb.append(sep);
      sb.append(ind);
      for(final int ch : row) {
        if (ch == 0) { sb.append("*"); }
        else if (ch == cars.length) { sb.append("."); }
        else if (ch > 61) { sb.append((char)(128+ch-62)); }
        else if (ch > 35) { sb.append((char)(65+ch-36)); }
        else if (ch > 9) { sb.append((char)(97+ch-10)); }
        else { sb.append(ch); }
      }
      sep = "\n";
    }

  }

  /**
   *  Checks that two boards have the same cars in the same places.
   */
  @Override public boolean equals(Object o) {
    if (!(o instanceof BoardState)) { return false; }
    final BoardState that = (BoardState)o;

    if (placed() != that.placed()) { return false; }
    for(int i=0; i<placed(); i++) {
      if (placement(i) != that.placement(i)) { return false; }
    }
    return true;
  }

  /**
   *  Output representations are unique enough, so just build and
   *  return its hash code.
   */
  @Override public int hashCode() {
    return toString("").hashCode();
  }

  /**
   *  Return a string with the states of the board squares, for lazy
   *  hashing of boards.
   */
  public String boardString() {
    return toString("");
  }
}
