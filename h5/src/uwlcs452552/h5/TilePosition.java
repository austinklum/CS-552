// TilePosition.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;

/**
 * Record class for communicating a position in the grid.
 */
public class TilePosition {
  protected final int col, row;

  /**
   * Sole constructor, assembling the move record.
   */
  public TilePosition(int col, int row) {
    this.col = col;
    this.row = row;
  }

  /** Index of the row */
  public int col() { return col; }

  /** Index of the column */
  public int row() { return row; }

  @Override public int hashCode() { return col*1000+row; }

  @Override public String toString() {
    return "[row=" + row + ",col=" + col + "]";
  }
}

