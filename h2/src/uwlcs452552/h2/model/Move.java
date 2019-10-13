
package uwlcs452552.h2.model;
import java.util.List;

public class Move {

  private final Direction direction;
  private final PlacedCar car;

  public Move(Direction direction, PlacedCar car) {
    this.direction = direction;
    this.car = car;
  }

  public Direction getDirection() { return direction; }
  public PlacedCar getCar() { return car; }

  public BoardState apply(BoardState before) {
    final int placed = before.placed();
    final PlacedCar[] newPlacements = new PlacedCar[placed];
    for(int i=0; i<placed; i++) {
      final PlacedCar thisCar = before.placement(i);
      newPlacements[i] = (car == thisCar)
          ? direction.apply(thisCar,before) : thisCar;
    }
    return new BoardState(newPlacements, before.getBoardSize());
  }

  @Override public String toString() {
    if (car == null) {
      return "NONE";
    } else if (direction == null) {
      return "no-op for " + car.getName();
    } else {
      return "move " + car.getName() + " " + direction.toString();
    }
  }

  public static String formatMoves(List<Move> moves) {
    if (moves.size() == 0) {
      return "NO SOLUTION";
    }

    final StringBuilder sb = new StringBuilder();
    String sep = "";
    for(final Move move : moves) {
      sb.append(sep);
      sb.append(move.toString());
      sep = ", ";
    }
    return sb.toString();
  }

  // =================================================================

  public static enum Direction {
    UP(-1, 0, "up"), DOWN(1, 0, "down"), LEFT(0, -1, "left"),
    RIGHT(0, 1, "right"), NONE(0, 0, "none");

    private final int dRow, dCol;
    private final String name;
    Direction(int dRow, int dCol, String name) {
      this.dRow = dRow;
      this.dCol = dCol;
      this.name = name;
    }
    public int getRowChange() { return dRow; }
    public int getColChange() { return dCol; }
    @Override public String toString() { return name; }

    public PlacedCar apply(PlacedCar car, BoardState before) {
      final int
          boardSize = before.getBoardSize(),
          newRow = dRow + car.getRow(),
          newCol = dCol + car.getCol();
      if (newRow<0 || newRow>=boardSize || newCol<0 || newCol>=boardSize) {
        throw new IllegalMoveException(this, car);
      } else if (before.filledAt(impactFor(dRow, car.getRow(), car),
                                 impactFor(dCol, car.getCol(), car))) {
        throw new MoveCollisionException(this, car, before);
      }

      return new PlacedCar(car.getId(), car.getName(), car.isVertical(),
                           car.getLength(), newRow, newCol,
                           before.getBoardSize());
    }

    public static int impactFor(int delta, int base, PlacedCar car) {
      if (delta <= 0) {
        return base + delta;
      } else {
        return base + car.getLength();
      }
    }
  };

  public static final Move NONE = new Move(Direction.NONE, null);
}
