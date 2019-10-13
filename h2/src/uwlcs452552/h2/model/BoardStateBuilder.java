
package uwlcs452552.h2.model;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import uwlcs452552.search.SearchFailureException;
import uwlcs452552.h2.BreadthFirstFinder;

public class BoardStateBuilder {

  private final ArrayList<PlacedCar> cars = new ArrayList<>();
  private int nextId = 0;
  private final int[][] checker;
  private final int boardSize;

  public BoardStateBuilder(int boardSize) {
    this.boardSize = boardSize;
    this.checker = new int[boardSize][boardSize];
    for(int i=0; i<boardSize; i++) {
      for(int j=0; j<boardSize; j++) {
        this.checker[i][j] = -1;
      }
    }
  }

  public BoardStateBuilder(int boardSize, int ourRow, int ourLength,
                           int startColumn) {
    this(boardSize);
    addHorizontal("our car", ourRow, ourLength, startColumn);
  }

  public static BoardStateBuilder standard(int startColumn) {
    return new BoardStateBuilder(BoardState.DEFAULT_BOARD_SIZE,
                                 BoardState.DEFAULT_OUR_ROW,
                                 BoardState.DEFAULT_OUR_DEFAULT_LENGTH,
                                 startColumn);
  }

  public BoardStateBuilder addHorizontal(int length, int row, int firstCol) {
    int id = nextId++;
    assertFreeInRow("Car "+id, row, firstCol, firstCol+length-1);
    cars.add(new PlacedCar(id, false, length, row, firstCol, boardSize));
    reserveInRow(id, row, firstCol, firstCol+length-1);
    return this;
  }

  public BoardStateBuilder
      addHorizontal(String carName, int length, int row, int firstCol) {
    int id = nextId++;
    assertFreeInRow(carName, row, firstCol, firstCol+length-1);
    cars.add(new PlacedCar(id, carName, false, length,
                           row, firstCol, boardSize));
    reserveInRow(id, row, firstCol, firstCol+length-1);
    return this;
  }

  public BoardStateBuilder addVertical(int length, int firstRow, int col) {
    int id = nextId++;
    assertFreeInCol("Car "+id, col, firstRow, firstRow+length-1);
    cars.add(new PlacedCar(id, true, length, firstRow, col, boardSize));
    reserveInCol(id, col, firstRow, firstRow+length-1);
    return this;
  }

  public BoardStateBuilder
      addVertical(String carName, int length, int firstRow, int col) {
    int id = nextId++;
    assertFreeInCol(carName, col, firstRow, firstRow+length-1);
    cars.add(new PlacedCar(id, carName, true, length,
                           firstRow, col, boardSize));
    reserveInCol(id, col, firstRow, firstRow+length-1);
    return this;
  }

  public void assertFreeInRow(String carName, int row,
                              int firstCol, int lastCol) {
    for(int c=firstCol; c<=lastCol; c++) {
      if (checker[row][c]>-1) {
        throw new PlacementCollisionException(carName, row, c, this);
      }
    }
  }

  public void assertFreeInCol(String carName, int col,
                              int firstRow, int lastRow) {
    for(int r=firstRow; r<=lastRow; r++) {
      if (checker[r][col]>-1) {
        throw new PlacementCollisionException(carName, r, col, this);
      }
    }
  }

  public void reserveInRow(int id, int row, int firstCol, int lastCol) {
    for(int c=firstCol; c<=lastCol; c++) {
      checker[row][c] = id;
    }
  }

  public void reserveInCol(int id, int col, int firstRow, int lastRow) {
    for(int r=firstRow; r<=lastRow; r++) {
      checker[r][col] = id;
    }
  }

  public BoardStateBuilder vCar(String color, int firstRow, int col) {
    addVertical(color + " car", 2, firstRow, col);
    return this;
  }

  public BoardStateBuilder vTruck(String color, int firstRow, int col) {
    addVertical(color + " truck", 3, firstRow, col);
    return this;
  }

  public BoardStateBuilder hCar(String color, int row, int firstCol) {
    addHorizontal(color + " car", 2, row, firstCol);
    return this;
  }

  public BoardStateBuilder hTruck(String color, int row, int firstCol) {
    addHorizontal(color + " truck", 3, row, firstCol);
    return this;
  }

  /**
   *  Returns the (immutable) {@linkplain BoardState board} described
   *  in this builder.
   */
  public BoardState board() {
    return new BoardState(cars.toArray(PlacedCar.EMPTY_ARRAY), boardSize);
  }

  // -----------------------------------------------------------------
  // Methods and fields for generating random boards below.

  private static final Random rnd = new Random();

  /** Write down a Java expression for this builder state. */
  public String reify() {
    final StringBuilder sb = new StringBuilder();

    sb.append("(new BoardStateBuilder(");
    sb.append(boardSize);
    sb.append("))");

    for (final PlacedCar car : cars) {

      sb.append("\n.add");
      if (car.isVertical()) {
        sb.append("Vertical");
      } else {
        sb.append("Horizontal");
      }
      sb.append("(\"");
      sb.append(car.getName());
      sb.append("\",");
      sb.append(car.getLength());
      sb.append(",");
      sb.append(car.getRow());
      sb.append(",");
      sb.append(car.getCol());
      sb.append(")");
    }

    return sb.toString();
  }

  public boolean solvable() {
    final BreadthFirstFinder finder = new BreadthFirstFinder();
    return finder.solvable(board());
  }

  public int addCarsWhileSolvable() {
    if (!solvable()) {
      return -1;
    }

    // Add a car to start off
    addRandomCar();
    int carsAdded = 0;

    // Keep adding cars as long as the board is still solvable
    ADDING:
    while (solvable()) {
      boolean added = addRandomCar();
      if (!added) { break ADDING; }
      carsAdded += 1;
      // System.out.print(" (" + carsAdded + "/" + openSpots().size() + ")");
    }

    // Undo the last one
    if (!solvable()) {
      undoLastCar();
    } else {
      carsAdded += 1;
    }

    return carsAdded;
  }

  /** Back out of the last (probably random) add */
  public void undoLastCar() {
    cars.remove(cars.size()-1);
  }

  /** Add a car to a random open spots on this builder */
  public boolean addRandomCar() {
    final ArrayList<Coord> spots = openSpots();
    final int spotLen = spots.size();
    if (spotLen == 0) { return false; }

    // So pull a spot and see what can go there
    final Coord c = spots.get(rnd.nextInt(spotLen));
    final int x=c.x, y=c.y;
    final boolean canV = checker[1+x][y]<0, canH = checker[x][1+y]<0;

    // If we can put it either way, flip a coin to see which we want
    boolean doH=false;
    if (canV && canH) {
      doH=rnd.nextBoolean();
    }

    if (doH || !canV) {
      int len = 2;
      // Flip a coin again to pick between placing car and truck
      final boolean canThree = 2+y<boardSize && checker[x][2+y]<0;
      if (canThree && rnd.nextBoolean()) {
        len = 3;
      }

      addHorizontal("random", len, x, y);
    } else {
      int len = 2;
      // Flip a coin again to pick between placing car and truck
      final boolean canThree = 2+x<boardSize && checker[2+x][y]<0;
      if (canThree && rnd.nextBoolean()) {
        len = 3;
      }

      addVertical("random", len, x, y);
    }

    return true;
  }

  /** Pull a list of spots where a another vehicle could go */
  public ArrayList<Coord> openSpots() {
    final ArrayList<Coord> spots = new ArrayList<>();
    // Go through the rows and columns
    for(int i=0; i<boardSize-1; i++) {
      for(int j=0; j<boardSize-1; j++) {
        if (checker[i][j]<0 && (checker[1+i][j]<0 || checker[i][1+j]<0)) {
          spots.add(new Coord(i,j));
        }
      }
    }
    return spots;
  }

  private static final class Coord {
    final int x, y;
    Coord(int x, int y) { this.x=x; this.y=y; }
  }

  public static final void main(String[] args) {
    final BreadthFirstFinder finder = new BreadthFirstFinder();
    for(int i=0; i<50; i++) {
      System.out.print("Try " + (1+i) + ": ");
      BoardStateBuilder builder = new BoardStateBuilder(20,2,2,0);
      int added = builder.addCarsWhileSolvable();
      System.out.print(" added " + added);
      added = builder.addCarsWhileSolvable();
      System.out.print(" then " + added);
      added = builder.addCarsWhileSolvable();
      System.out.println(" then " + added);
      try {
        System.out.println("Solution steps: " + finder.search(builder.board()).fillPath().size());
      } catch (SearchFailureException e) {
      }
      // System.out.println(builder.reify());
    }
  }
}
