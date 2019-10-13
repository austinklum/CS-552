
package uwlcs452552.h2.model;

public class PlacementCollisionException extends RuntimeException {

  private final String name;
  private final int row;
  private final int column;
  private final BoardStateBuilder builder;

  public PlacementCollisionException(String name, int row, int column,
                                     BoardStateBuilder builder) {
    this.name = name;
    this.row = row;
    this.column = column;
    this.builder = builder;
  }

  public String getName() { return name; }
  public int getRow() { return row; }
  public int getColumn() { return column; }
  public BoardStateBuilder getBuilder() { return builder; }

  public String toString() {
    return "collision for " + name + " at " + row + "," + column;
  }
}

