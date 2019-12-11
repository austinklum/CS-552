// AgentPosition.java
//
// 2019, for CS452/552, JPH Maraist
package uwlcs452552.h5;
import java.util.Optional;

/**
 * Record class for an agent's position on the board.  As in {@link
 * Tile}, the eight path endpoints on a tile are numbered from 0 to 7
 * from the left path on the upper edge, and proceeding clockwise.
 */
public class AgentPosition extends TilePosition {
  private final int slot;

  /**
   * Sole constructor, assembling the move record.
   */
  public AgentPosition(int col, int row, int slot) {
    super(col, row);
    this.slot = slot;
  }

  /** Slot on the given tile. */
  public int slot() { return slot; }

  /**
   *  Used in Board.apply to advance a stone through connected paths.
   *
   * @return The empty option if the resulting spot is off the board,
   * otherwise a non-empty option with the new agent position.
   */
  public Optional<AgentPosition> advanceInGrid(Move[][] grid) {
    // Get position opposite current one
    final int
        newX = col + dColOpposite(slot),
        newY = row + dRowOpposite(slot);
    // System.out.printf("- Opposite tile at %d,%d\n", newY, newX);

    // If off the grid, return Optional.empty()
    if (newX<0 || newX>=grid.length || newY<0 || newY>=grid.length) {
      // System.out.printf("- Off-grid\n");
      return Optional.empty();
    }

    // If on empty grid space, return this
    if (grid[newX][newY] == null) {
      // System.out.printf("- Opposite tile [%d][%d] is empty --- accepting this slot\n", newX, newY);
      return Optional.of(this);
    } else {
      // System.out.printf("- Opposite tile [%d][%d] is not empty --- recurring\n", newX, newY);
    }

    // If on filled grid space, recur on that grid space, return
    // result
    final int
        oppositeSlot = slotOpposite[slot],
        newSlot = grid[newX][newY].getConnectedSlot(oppositeSlot);
    // System.out.printf("- Opposite slot is %d, connected to slot %d\n",
    //                   oppositeSlot, newSlot);
    return new AgentPosition(newX, newY, newSlot).advanceInGrid(grid);
  }

  /**
   * Calculate the {@link TilePosition} opposite this agent's
   * position, and return whether it is the same as the given tile.
   */
  public boolean isOpposite(TilePosition p) {
    return getOppositeTilePositionRow() == p.row()
        && getOppositeTilePositionColumn() == p.col();
  }

  public TilePosition getOppositeTilePosition() {
    return new TilePosition(getOppositeTilePositionColumn(),
                            getOppositeTilePositionRow());
  }

  public int getOppositeTilePositionRow() {
    return row + dRowOpposite(slot);
  }

  public int getOppositeTilePositionColumn() {
    return col + dColOpposite(slot);
  }

  private static int dRowOpposite(int s) {
    if (s<2) { return -1; }
    else if (s==4 || s==5) { return 1; }
    else return 0;
  }

  private static int dColOpposite(int s) {
    if (s>5) { return -1; }
    else if (s==2 || s==3) { return 1; }
    else return 0;
  }

  private static final int[] slotOpposite = { 5, 4, 7, 6, 1, 0, 3, 2 };

  @Override public int hashCode() { return super.hashCode()*8 + slot; }

  @Override public String toString() {
    return "[row=" + row+ ",col=" + col + ",slot=" + slot + "]";
  }
}

