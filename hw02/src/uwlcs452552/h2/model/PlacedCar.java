
package uwlcs452552.h2.model;

public class PlacedCar {
  private final int id, length, row, col, boardSize;
  private final String name;
  private final boolean vertical;

  public PlacedCar(int id, boolean vertical, int length,
                   int row, int col, int size) {
    this(id, "Car "+Integer.toString(id), vertical, length, row, col, size);
  }

  public PlacedCar(int id, String name, boolean vertical, int length,
                   int row, int col, int boardSize) {
    this.id = id;
    this.name = name;
    this.vertical = vertical;
    this.length = length;
    this.row = row;
    this.col = col;
    this.boardSize = boardSize;
  }

  public boolean isTargetCar() { return getId() == 0; }
  public boolean atGoal() {
    return !vertical && getCol()+getLength() == boardSize;
  }

  public int getId() { return id; }
  public int getLength() { return length; }
  public int getRow() { return row; }
  public int getCol() { return col; }
  public String getName() { return name; }
  public boolean isVertical() { return vertical; }

  public static final PlacedCar[] EMPTY_ARRAY = new PlacedCar[0];


  @Override public int hashCode() {
    return 15*(2*(boardSize*(row*boardSize + col) + length)
               + (vertical ? 0 : 1)) + getId();
  }

  @Override public boolean equals(Object o) {
    if (!(o instanceof PlacedCar)) { return false; }
    final PlacedCar that = (PlacedCar)o;

    return getLength() == that.getLength()
        && getId() == that.getId()
        && getRow() == that.getRow()
        && getCol() == that.getCol()
        && isVertical() == that.isVertical();
  }

}
